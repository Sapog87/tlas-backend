package ru.axenix.service;

import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.axenix.dto.*;
import ru.axenix.rzd.response.result.Car;
import ru.axenix.rzd.response.scheme.CarSchema;
import ru.axenix.rzd.response.scheme.Schemes;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;
import static ru.axenix.util.TrainUtil.getType;

@Slf4j
@Service
@RequiredArgsConstructor
public class RzdCarService {
    private final RzdService rzdService;

    public Tickets getTickets(String origin, String destination, OffsetDateTime date, String train) {
        log.info("Start RzdCarService::places with origin {}, destination {}, date {}, train {}",
                origin, destination, date, train);

        var carPricing = rzdService.carPricing(origin, destination, date, train);
        var coaches = carPricing.getCars().stream()
                .collect(groupingBy(
                        Car::getCarNumber,
                        collectingAndThen(
                                toList(),
                                list -> {
                                    var car = list.get(0);
                                    var type = car.getCarType();
                                    var number = car.getCarNumber();
                                    var scheme = car.getRailwayCarSchemeId();
                                    var isTwoStorey = car.getIsTwoStorey();
                                    var schemeName = car.getCarSchemeName();
                                    var places = list.stream()
                                            .flatMap(c -> c.getFreePlacesByCompartments().stream()
                                                    .flatMap(place ->
                                                            stream(place.getPlaces().split(","))
                                                                    .filter(StringUtils::hasText)
                                                                    .map(String::trim)
                                                                    .map(p -> new Place()
                                                                            .setCompartment(place.getCompartmentNumber())
                                                                            .setKopecks((int) (c.getMinPrice() * 100))
                                                                            .setType(c.getCarPlaceType())
                                                                            .setNumber(p)
                                                                            .setServiceClass(c.getServiceClass())
                                                                    )
                                                    )
                                            ).toList();

                                    return new Coach()
                                            .setNumber(number)
                                            .setCarType(getType(type))
                                            .setFreePlaces(places)
                                            .setSchemeId(scheme)
                                            .setSchemeName(schemeName)
                                            .setIsTwoStorey(isTwoStorey);
                                }
                        )))
                .values().stream()
                .filter(coach -> coach.getCarType() != CarType.UNKNOWN)
                .toList();

        var schemes = coaches.stream()
                .filter(coach -> coach.getSchemeName() != null)
                .filter(distinctByKey(Coach::getSchemeName))
                .flatMap(coach ->
                        createSchemesFromCoach(
                                coach,
                                carPricing.getTrain().getTrainNumber()
                        )
                ).toList();

        return new Tickets()
                .setVehicle(carPricing.getTrain().getTrainName())
                .setRaceNumber(carPricing.getTrain().getTrainNumber())
                .setSchemes(schemes)
                .setCoaches(coaches);
    }

    private Stream<Scheme> createSchemesFromCoach(Coach coach, String trainNum) {
        if (coach.getSchemeId() != null) {
            return buildSchemesWithSchemeId(coach);
        } else {
            return buildSchemesWithoutSchemeId(coach, trainNum);
        }
    }

    private Stream<Scheme> buildSchemesWithoutSchemeId(Coach coach, String trainNum) {
        var schemesRzd = rzdService.schemes(coach.getSchemeName());
        var scheme = getScheme(coach, schemesRzd, trainNum);

        if (scheme == null) {
            return Stream.empty();
        }

        if (Boolean.TRUE.equals(coach.getIsTwoStorey())) {
            return Stream.of(
                    new Scheme()
                            .setName(coach.getSchemeName())
                            .setSvg(rzdService.schemeSvg(getLast(scheme.getContent().get(0).getEnricoRecord().getSchemaPC1Horizontal())))
                            .setIsFirstStorey(true),
                    new Scheme()
                            .setName(coach.getSchemeName())
                            .setSvg(rzdService.schemeSvg(getLast(scheme.getContent().get(0).getEnricoRecord().getSchemaPC2Horizontal())))
                            .setIsFirstStorey(false)
            );
        }

        return Stream.of(new Scheme()
                .setName(coach.getSchemeName())
                .setSvg(rzdService.schemeSvg(getLast(scheme.getContent().get(0).getEnricoRecord().getSchemaPC1Horizontal())))
                .setIsFirstStorey(true)
        );
    }

    private Stream<Scheme> buildSchemesWithSchemeId(Coach coach) {
        if (Boolean.TRUE.equals(coach.getIsTwoStorey())) {
            return Stream.of(
                    new Scheme()
                            .setName(coach.getSchemeName())
                            .setSvg(rzdService.schemeSvg(coach.getSchemeId(), true))
                            .setIsFirstStorey(true),
                    new Scheme()
                            .setName(coach.getSchemeName())
                            .setSvg(rzdService.schemeSvg(coach.getSchemeId(), false))
                            .setIsFirstStorey(false)
            );
        }

        return Stream.of(new Scheme()
                .setId(coach.getSchemeId())
                .setName(coach.getSchemeName())
                .setSvg(rzdService.schemeSvg(coach.getSchemeId(), true))
                .setIsFirstStorey(true)
        );
    }

    private String getLast(String a) {
        return Streams.findLast(stream(a.split("/"))).orElse(null);
    }

    private CarSchema getScheme(Coach coach, Schemes schemesRzd, String trainNum) {
        var allTrain = schemesRzd.carSchemas.stream()
                .filter(x -> Objects.equals(x.getTrainNum(), "all"))
                .findFirst()
                .orElse(null);

        var t = schemesRzd.carSchemas.stream()
                .filter(x -> Objects.equals(x.getTrainNum(), trainNum))
                .toList();

        if (t.isEmpty()) {
            return allTrain;
        }

        var allCar = t.stream()
                .filter(x -> Objects.equals(x.getCarNum(), "all"))
                .findFirst()
                .orElse(null);

        var tt = schemesRzd.carSchemas.stream()
                .filter(x -> Objects.equals(x.getCarNum(), coach.getNumber()))
                .toList();

        if (tt.isEmpty()) {
            return allCar;
        }

        return tt.get(0);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        var seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
