package ru.axenix.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.axenix.dto.CarType;
import ru.axenix.dto.Transport;
import ru.axenix.rzd.request.mmp.*;
import ru.axenix.rzd.request.p.PRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@UtilityClass
public class TrainUtil {
    private static final SystemParams systemParams;
    private static final List<Filter> filters;

    static {
        systemParams = SystemParams.builder()
                .searchViaAr("SVA_DONT_SEARCH")
                .searchViaGraph("SVG_SEARCH_WITH_DETAIL")
                .detailedLocation(true)
                .debug(false)
                .build();
        filters = List.of(Filter.builder()
                .exactFilter(ExactFilter.builder()
                        .paramValues(List.of("b2brails", "cbdpr"))
                        .build())
                .paramName("route.provider.key")
                .build());
    }

    public static PRequest getOneWayPRequest(String expressCodeOrigin, String expressCodeDestination, LocalDate date) {
        return PRequest.builder()
                .origin(expressCodeOrigin)
                .destination(expressCodeDestination)
                .departureDate(LocalDateTime.of(date, LocalTime.of(0, 0, 0)))
                .carGrouping("Group")
                .build();
    }

    public static MmpRequest getOneWayMmpRequest(String start, String finish, LocalDate date) {
        return OneWayMmpRequest.builder()
                .maxResults(200)
                .minTripsInLeg(2)
                .maxTripsInLeg(4)
                .systemParams(systemParams)
                .filters(filters)
                .startLocation(getLocation(start))
                .finishLocation(getLocation(finish))
                .startDatetimeRange(getStartDatetimeRange(date))
                .build();
    }

    public static MmpRequest getRoundMmpRequest(String start, String finish, LocalDate forward, LocalDate backward) {
        return RoundMmpRequest.builder()
                .maxResults(200)
                .minTripsInLeg(2)
                .maxTripsInLeg(4)
                .systemParams(systemParams)
                .filters(filters)
                .startLocation(getLocation(start))
                .finishLocation(getLocation(finish))
                .forwardStartDatetimeRange(getStartDatetimeRange(forward))
                .backwardStartDatetimeRange(getStartDatetimeRange(backward))
                .build();
    }

    public static CarType getType(String type) {
        return switch (type) {
            case "Soft" -> CarType.SOFT;
            case "Luxury" -> CarType.LUXURY;
            case "Compartment" -> CarType.COMPARTMENT;
            case "ReservedSeat" -> CarType.RESERVED_SEAT;
            case "Sedentary" -> CarType.SEDENTARY;
            default -> {
                log.warn("Unknown train product type {}", type);
                yield CarType.UNKNOWN;
            }
        };
    }

    public static Transport getTransport(String transport) {
        return switch (transport) {
            case "train" -> Transport.TRAIN;
            case "plane" -> Transport.PLANE;
            case "suburban" -> Transport.SUBURBAN;
            case "bus" -> Transport.BUS;
            default -> throw new IllegalStateException(transport);
        };
    }


    public static CarPricingRequest getCarPricingRequest(String originCode, String destinationCode, OffsetDateTime departureDate, String trainNumber) {
        return CarPricingRequest.builder()
                .originCode(originCode)
                .destinationCode(destinationCode)
                .departureTime(departureDate)
                .trainNumber(trainNumber)
                .build();
    }

    private static StartDatetimeRange getStartDatetimeRange(LocalDate date) {
        return StartDatetimeRange.builder()
                .from(LocalDateTime.of(date, LocalTime.of(0, 0, 0)))
                .to(LocalDateTime.of(date, LocalTime.of(23, 59, 59)))
                .build();
    }

    private static Location getLocation(String start) {
        var station = Station.builder()
                .key(start)
                .build();
        return Location.builder()
                .station(station)
                .build();
    }
}
