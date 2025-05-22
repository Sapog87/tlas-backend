package ru.axenix.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.axenix.dto.Product;
import ru.axenix.dto.Route;
import ru.axenix.dto.Segment;
import ru.axenix.dto.*;
import ru.axenix.exception.ResultException;
import ru.axenix.exception.YandexCodeNotFoundException;
import ru.axenix.rzd.response.result.*;
import ru.axenix.yandex.search.MultiRouteSegment;
import ru.axenix.yandex.search.Response;
import ru.axenix.yandex.search.SingleRouteSegment;
import ru.axenix.yandex.search.Transfer;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;
import static ru.axenix.config.RedisCacheConfig.RZD_ROUTE;
import static ru.axenix.config.RedisCacheConfig.YANDEX_ROUTE;
import static ru.axenix.security.HttpRequestIdFilter.REQUEST_ID;
import static ru.axenix.util.TrainUtil.getTransport;
import static ru.axenix.util.TrainUtil.getType;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {
    private final RzdService rzdService;
    private final YandexService yandexService;
    private final YandexCodeService yandexCodeService;
    private final SearchHistoryService searchHistoryService;

    @Cacheable(RZD_ROUTE)
    public List<Route> findOneWayRoutesRzd(String fromYandexCode, String toYandexCode, LocalDate date) {
        var rid = MDC.get(REQUEST_ID);
        var futures = List.of(
                CompletableFuture.supplyAsync(() -> {
                    MDC.put(REQUEST_ID, rid);
                    return findOneWayMmpRoutesRzdInternal(fromYandexCode, toYandexCode, date);
                }),
                CompletableFuture.supplyAsync(() -> {
                    MDC.put(REQUEST_ID, rid);
                    return findOneWayPRoutesRzdInternal(fromYandexCode, toYandexCode, date);
                })
        );

        try {
            var result = combineFutures(futures).get();
            saveSearch(fromYandexCode, toYandexCode);
            return result;
        } catch (Exception e) {
            throw new ResultException(e);
        }
    }

    @Cacheable(YANDEX_ROUTE)
    public List<Route> findOneWayRoutesYandex(String fromYandexCode, String toYandexCode, LocalDate date) {
        var routes = findOneWayRoutesYandexInternal(fromYandexCode, toYandexCode, date);
        saveSearch(fromYandexCode, toYandexCode);
        return routes;
    }

    public <T> CompletableFuture<List<T>> combineFutures(List<CompletableFuture<List<T>>> futures) {
        List<CompletableFuture<Optional<List<T>>>> handledFutures = futures.stream()
                .map(future -> future
                        .thenApply(Optional::of)
                        .exceptionally(ex -> {
                            log.warn(ex.getMessage(), ex);
                            return Optional.empty();
                        }))
                .toList();

        return CompletableFuture
                .allOf(handledFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    List<Optional<List<T>>> results = handledFutures.stream()
                            .map(CompletableFuture::join)
                            .toList();

                    long successCount = results.stream().filter(Optional::isPresent).count();
                    if (successCount == 0) {
                        throw new ResultException("All requests failed");
                    }

                    return results.stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .flatMap(List::stream)
                            .toList();
                });
    }

    private void saveSearch(String fromYandexCode, String toYandexCode) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                searchHistoryService.addSearchHistory(authentication.getName(), fromYandexCode, toYandexCode);
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<Route> findOneWayMmpRoutesRzdInternal(String fromYandexCode, String toYandexCode, LocalDate date) {
        var from = Optional.ofNullable(yandexCodeService.getCodes(fromYandexCode))
                .orElseThrow(() -> new YandexCodeNotFoundException(fromYandexCode));
        var to = Optional.ofNullable(yandexCodeService.getCodes(toYandexCode))
                .orElseThrow(() -> new YandexCodeNotFoundException(toYandexCode));

        if (isNull(from.nodeId()) || isNull(to.nodeId())) {
            return List.of();
        }

        var results = rzdService.onewayMmpRoutes(
                from.nodeId(),
                to.nodeId(),
                date
        );

        return mapToRoutes(results);
    }

    private List<Route> findOneWayPRoutesRzdInternal(String fromYandexCode, String toYandexCode, LocalDate date) {
        var from = Optional.ofNullable(yandexCodeService.getCodes(fromYandexCode))
                .orElseThrow(() -> new YandexCodeNotFoundException(fromYandexCode));
        var to = Optional.ofNullable(yandexCodeService.getCodes(toYandexCode))
                .orElseThrow(() -> new YandexCodeNotFoundException(toYandexCode));

        if (isNull(from.express()) || isNull(to.express())) {
            return List.of();
        }

        var results = rzdService.onewayPRoutes(
                from.express(),
                to.express(),
                date
        );
        return mapToRoutes(results);
    }

    private List<Route> mapToRoutes(List<Result> results) {
        return results.stream()
                .map(result -> {
                    var leg = result.getLegs().stream()
                            .findFirst()
                            .orElseThrow(() -> new ResultException("Не удалось найти leg в " + results));

                    var segments = result.getRoutes().stream()
                            .flatMap(r -> r.getSegments().stream())
                            .flatMap(s -> s.getTrips().stream())
                            .map(trip -> {
                                var segment = new Segment()
                                        .setStartStation(trip.getStartLocation().getStation().getNameRu())
                                        .setFinishStation(trip.getFinishLocation().getStation().getNameRu())
                                        .setRaceNumber(trip.getRaceNumber())
                                        .setStartDateTime(trip.getStartDatetime())
                                        .setFinishDateTime(trip.getFinishDatetime())
                                        .setStartCity(trip.getStartLocation().getParentCity().getNameRu())
                                        .setFinishCity(trip.getFinishLocation().getParentCity().getNameRu());
                                if (trip.getRawData() != null) {
                                    segment.setTransport(Transport.TRAIN)
                                            .setCarrier(getTitleFromRzd(trip.getRawData().getPricing().getTrains().get(0).getCarriers().get(0)))
                                            .setOriginCode(trip.getRawData().getPricing().getOriginStationCode())
                                            .setDestinationCode(trip.getRawData().getPricing().getDestinationStationCode());
                                } else {
                                    segment.setTransport(Transport.SUBURBAN);
                                }
                                segment.setProducts(trip.getProducts().stream()
                                        .map(product -> {
                                            if (product.getTrainCarType() != null) {
                                                var trainProduct = new TrainProduct();
                                                trainProduct.setType(ProductType.TRAIN);
                                                trainProduct.setCarType(getType(product.getTrainCarType().getKey()));
                                                trainProduct.setPriceInKopecks(Integer.parseInt(product.getPrice().getKopecks()));
                                                trainProduct.setFreePlaces(product.getFreePlaces());
                                                return (Product) trainProduct;
                                            }
                                            return null;
                                        })
                                        .filter(Objects::nonNull)
                                        .toList());
                                return segment;
                            }).toList();

                    return new Route()
                            .setSegments(segments)
                            .setTransfers(segments.size() - 1)
                            .setStartLocation(leg.getStartLocation().getStation().getNameRu())
                            .setFinishLocation(leg.getFinishLocation().getStation().getNameRu())
                            .setStartDateTime(leg.getStartDatetime())
                            .setFinishDateTime(leg.getFinishDatetime());
                }).toList();
    }

    private List<Route> mapToRoutes(RailwayV1SearchTrainPricing result) {
        return result.getTrains().stream()
                .map(train -> {
                    var segments = List.of(
                            new Segment()
                                    .setVehicle(train.getTrainName())
                                    .setStartStation(train.getOriginStationName())
                                    .setOriginCode(train.getOriginStationCode())
                                    .setFinishStation(train.getDestinationStationName())
                                    .setDestinationCode(train.getDestinationStationCode())
                                    .setStartDateTime(OffsetDateTime.of(train.getDepartureDateTime(), ZoneOffset.ofHours(3)))
                                    .setFinishDateTime(OffsetDateTime.of(train.getArrivalDateTime(), ZoneOffset.ofHours(3)))
                                    .setCarrier(getTitleFromRzd(train.getCarriers().get(0)))
                                    .setProducts(getProducts(train))
                                    .setRaceNumber(train.getTrainNumber())
                                    .setTransport("Express3".equals(train.getBookingSystem()) ? Transport.TRAIN : Transport.SUBURBAN)
                                    .setStartCity(result.getOriginStationInfo().getStationName())
                                    .setFinishCity(result.getDestinationStationInfo().getStationName())
                    );

                    return new Route()
                            .setSegments(segments)
                            .setTransfers(0)
                            .setStartLocation(train.getOriginStationName())
                            .setFinishLocation(train.getDestinationStationName())
                            .setStartDateTime(OffsetDateTime.of(train.getDepartureDateTime(), ZoneOffset.ofHours(3)))
                            .setFinishDateTime(OffsetDateTime.of(train.getArrivalDateTime(), ZoneOffset.ofHours(3)));
                }).toList();
    }

    private List<Route> findOneWayRoutesYandexInternal(String fromYandexCode, String toYandexCode, LocalDate date) {
        var from = Optional.ofNullable(yandexCodeService.getCodes(fromYandexCode))
                .orElseThrow(() -> new YandexCodeNotFoundException(fromYandexCode));
        var to = Optional.ofNullable(yandexCodeService.getCodes(toYandexCode))
                .orElseThrow(() -> new YandexCodeNotFoundException(toYandexCode));

        var result = yandexService.getResult(from.yandexCode(), to.yandexCode(), date);
        var routes = mapResponseToResults(result);

        var now = OffsetDateTime.now();
        var actual = routes.stream()
                .filter(route -> route.getStartDateTime().isAfter(now))
                .toList();

        var rid = MDC.get(REQUEST_ID);
        CompletableFuture.allOf(
                actual.stream()
                        .flatMap(route -> route.getSegments().stream())
                        .filter(segment -> segment.getTransport() == Transport.TRAIN)
                        .filter(segment ->
                                Objects.nonNull(segment.getOriginCode())
                                && Objects.nonNull(segment.getDestinationCode())
                                && Objects.nonNull(segment.getRaceNumber())
                        )
                        .map(segment ->
                                CompletableFuture
                                        .supplyAsync(() -> {
                                                    MDC.put(REQUEST_ID, rid);
                                                    return carsInfo(
                                                            segment.getOriginCode(),
                                                            segment.getDestinationCode(),
                                                            segment.getStartDateTime(),
                                                            segment.getRaceNumber()
                                                    );
                                                }
                                        )
                                        .exceptionally(ex -> {
                                            log.warn(ex.getMessage(), ex);
                                            return List.of();
                                        })
                                        .thenAccept(segment::setProducts)
                        ).toArray(CompletableFuture[]::new)
        ).join();

        return actual.stream()
                .filter(route ->
                        route.getSegments().stream()
                                .allMatch(segment ->
                                        segment.getTransport() != Transport.TRAIN
                                        || !segment.getProducts().isEmpty()
                                ))
                .toList();
    }

    private List<Route> mapResponseToResults(Response result) {
        return result.getSegments().stream()
                .filter(segment ->
                        segment instanceof SingleRouteSegment routeSegment
                        && "plane".equals(routeSegment.getThread().getTransportType())
                        || segment instanceof MultiRouteSegment multiRouteSegment
                           && !multiRouteSegment.getTransportTypes().stream().allMatch("train"::equals))
                .map(s -> {
                            if (s instanceof SingleRouteSegment segment) {
                                return new Route()
                                        .setTransfers(0)
                                        .setStartDateTime(segment.getDeparture())
                                        .setFinishDateTime(segment.getArrival())
                                        .setStartLocation(segment.getFrom().getTitle())
                                        .setFinishLocation(segment.getTo().getTitle())
                                        .setSegments(List.of(new Segment()
                                                .setStartStation(segment.getFrom().getTitle())
                                                .setOriginCode(segment.getFrom().getCodes().getExpress())
                                                .setFinishStation(segment.getTo().getTitle())
                                                .setDestinationCode(segment.getTo().getCodes().getExpress())
                                                .setStartDateTime(segment.getDeparture())
                                                .setFinishDateTime(segment.getArrival())
                                                .setCarrier(getTitleFromYandex(segment.getThread().getCarrier().getTitle()))
                                                .setProducts(List.of())
                                                .setRaceNumber(segment.getThread().getNumber())
                                                .setTransport(getTransport(segment.getThread().getTransportType()))
                                                .setStartCity(segment.getFrom().getTitle())
                                                .setFinishCity(segment.getTo().getTitle()))
                                        );
                            }

                            if (s instanceof MultiRouteSegment segment) {
                                var details = segment.getSegments();
                                var segments = new ArrayList<Segment>();
                                for (int i = 0; i < details.size(); i += 2) {
                                    if (details.get(i) instanceof ru.axenix.yandex.search.Route route) {
                                        if (i == 0) {
                                            if (details.get(i + 1) instanceof Transfer transferNext) {
                                                segments.add(getSegment(
                                                                transferNext.getTransferFrom().getTitle(),
                                                                segment.getDepartureFrom().getTitle(),
                                                                route,
                                                                route.getFrom().getCodes().getExpress(),
                                                                transferNext.getTransferFrom().getCodes().getExpress()
                                                        )
                                                );
                                            }
                                        } else if (i == details.size() - 1) {
                                            if (details.get(i - 1) instanceof Transfer transferPrev) {
                                                segments.add(getSegment(
                                                                segment.getArrivalTo().getTitle(),
                                                                transferPrev.getTransferTo().getTitle(),
                                                                route,
                                                                transferPrev.getTransferTo().getCodes().getExpress(),
                                                                route.getTo().getCodes().getExpress()
                                                        )
                                                );
                                            }
                                        } else {
                                            if (details.get(i + 1) instanceof Transfer transferNext
                                                && details.get(i - 1) instanceof Transfer transferPrev
                                            ) {
                                                segments.add(getSegment(
                                                                transferNext.getTransferFrom().getTitle(),
                                                                transferPrev.getTransferTo().getTitle(),
                                                                route,
                                                                transferNext.getTransferFrom().getCodes().getExpress(),
                                                                transferPrev.getTransferTo().getCodes().getExpress()
                                                        )
                                                );
                                            }
                                        }
                                    } else {
                                        throw new IllegalStateException();
                                    }
                                }

                                return new Route()
                                        .setStartDateTime(segment.getDeparture())
                                        .setFinishDateTime(segment.getArrival())
                                        .setStartLocation(segments.get(0).getStartStation())
                                        .setFinishLocation(segments.get(segments.size() - 1).getFinishStation())
                                        .setTransfers(segment.getTransportTypes().size() - 1)
                                        .setSegments(segments);
                            }

                            throw new IllegalStateException();
                        }
                ).toList();
    }

    private static String getTitleFromYandex(String title) {
        return StringUtils.startsWithIgnoreCase(title, "ржд/")
                ? title.substring(4)
                : title;
    }

    private static String getTitleFromRzd(String title) {
        return "ГРАНДТ".equals(title)
                ? "ГРАНД"
                : title;
    }

    private static Segment getSegment(String titleNext, String titlePrev, ru.axenix.yandex.search.Route route, String originCode, String destinationCode) {
        var transport = getTransport(route.getThread().getTransportType());
        var vehicle = transport == Transport.TRAIN
                ? route.getThread().getTransportSubtype().getTitle()
                : route.getThread().getVehicle();
        return new Segment()
                .setVehicle(vehicle)
                .setStartStation(titlePrev)
                .setOriginCode(originCode)
                .setFinishStation(titleNext)
                .setDestinationCode(destinationCode)
                .setStartDateTime(route.getDeparture())
                .setFinishDateTime(route.getArrival())
                .setCarrier(getTitleFromYandex(route.getThread().getCarrier().getTitle()))
                .setProducts(List.of())
                .setRaceNumber(route.getThread().getNumber())
                .setTransport(transport)
                .setStartCity(route.getFrom().getTitle())
                .setFinishCity(route.getTo().getTitle());
    }

    private List<Product> carsInfo(String expressCodeOrigin, String expressCodeDestination, OffsetDateTime departureDate, String trainNumber) {
        var result = rzdService.carPricing(expressCodeOrigin, expressCodeDestination, departureDate, trainNumber);
        return getProducts(result);
    }

    private List<Product> getProducts(RailwayV1SearchCarPricing result) {
        return result.getCars().stream()
                .collect(groupingBy(
                        Car::getCarType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    var carName = list.isEmpty()
                                            ? ""
                                            : list.get(0).getCarType();
                                    var carType = getType(carName);
                                    if (carType == CarType.UNKNOWN) {
                                        return null;
                                    }
                                    int minPrice = list.stream()
                                            .mapToDouble(Car::getMinPrice)
                                            .mapToInt(x -> (int) (x * 100))
                                            .min().orElse(0);
                                    int totalPlaces = list.stream()
                                            .mapToInt(Car::getPlaceQuantity)
                                            .sum();
                                    return new TrainProduct()
                                            .setCarType(carType)
                                            .setFreePlaces(totalPlaces)
                                            .setPriceInKopecks(minPrice)
                                            .setType(ProductType.TRAIN);
                                }
                        )))
                .values().stream()
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Product> getProducts(Train train) {
        return train.getCarGroups().stream()
                .map(carGroup -> {
                    var carName = carGroup.getCarType();
                    var carType = getType(carName);
                    return new TrainProduct()
                            .setCarType(carType)
                            .setFreePlaces(carGroup.getTotalPlaceQuantity())
                            .setPriceInKopecks((int) (carGroup.getMinPrice() * 100));
                })
                .filter(p -> p.getCarType() != CarType.UNKNOWN)
                .map(Product.class::cast)
                .toList();
    }
}
