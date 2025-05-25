package ru.axenix.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.axenix.dto.ResultDto;
import ru.axenix.exception.ParseException;
import ru.axenix.exception.ReadException;
import ru.axenix.exception.ResultException;
import ru.axenix.rzd.request.mmp.CarPricingRequest;
import ru.axenix.rzd.request.mmp.MmpRequest;
import ru.axenix.rzd.request.p.PRequest;
import ru.axenix.rzd.response.Response;
import ru.axenix.rzd.response.error.Error;
import ru.axenix.rzd.response.result.RailwayV1SearchCarPricing;
import ru.axenix.rzd.response.result.RailwayV1SearchTrainPricing;
import ru.axenix.rzd.response.result.Result;
import ru.axenix.rzd.response.scheme.Schemes;
import ru.axenix.util.TrainUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static okhttp3.HttpUrl.parse;
import static ru.axenix.config.OkHttpConfig.MEDIA_TYPE_APPLICATION_JSON;
import static ru.axenix.config.RedisCacheConfig.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RzdService {
    private static final HttpUrl ONEWAY_MMP_ROUTES_URL = requireNonNull(parse("https://ticket.rzd.ru/apib2b/mmp/onewayRoutesStream/v1"));
    private static final HttpUrl TRAIN_PRICING_URL = requireNonNull(parse("https://ticket.rzd.ru/apib2b/p/Railway/V1/Search/TrainPricing"));
    private static final HttpUrl CAR_PRICING_URL = requireNonNull(parse("https://ticket.rzd.ru/apib2b/p/Railway/V1/Search/CarPricing"));
    private static final HttpUrl SVG_URL = requireNonNull(parse("https://ticket.rzd.ru/api/v1/carscheme/image"));
    private static final HttpUrl SVG_SCHEME_URL = requireNonNull(parse("https://ticket.rzd.ru/isdk/files"));
    private static final HttpUrl CAR_SCHEME_URL = requireNonNull(parse("https://ticket.rzd.ru/isdk/carSchemas"));
    private static final String LINE_BREAK = "\n";

    private final ObjectMapper objectMapper;
    private final OkHttpClient client;

    @Retryable(
            retryFor = ReadException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100)
    )
    @Cacheable(RZD_ROUTE_MMP)
    public List<ResultDto> onewayMmpRoutes(String start, String finish, LocalDate date) {
        log.info("Start TrainService::onewayMmpRoutes with start: {}, finish: {}, date: {}",
                start, finish, date);

        var request = TrainUtil.getOneWayMmpRequest(start, finish, date);
        return getResults(request);
    }

    @Retryable(
            retryFor = ReadException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100)
    )
    @Cacheable(RZD_ROUTE_P)
    public RailwayV1SearchTrainPricing onewayPRoutes(String expressCodeOrigin, String expressCodeDestination, LocalDate date) {
        log.info("Start TrainService::onewayPRoutes with start: {}, expressCodeOrigin: {}, expressCodeDestination: {}",
                expressCodeOrigin, expressCodeDestination, date);

        var request = TrainUtil.getOneWayPRequest(expressCodeOrigin, expressCodeDestination, date);
        return getResults(request);
    }

    @Retryable(
            retryFor = ReadException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100)
    )
    @Cacheable(RZD_CAR_PRICING)
    public RailwayV1SearchCarPricing carPricing(String expressCodeOrigin, String expressCodeDestination, OffsetDateTime departureDate, String trainNumber) {
        log.info("Start TrainService::carPricing with originCode {}, destinationCode {}, departureDate {}, trainNumber {}",
                expressCodeOrigin, expressCodeDestination, departureDate, trainNumber);

        var request = TrainUtil.getCarPricingRequest(expressCodeOrigin, expressCodeDestination, departureDate, trainNumber);
        return getResults(request);
    }

    @Retryable(
            retryFor = ReadException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100)
    )
    @Cacheable(RZD_CAR_SVG)
    public String schemeSvg(@NotNull Integer schemeId, boolean isFirstStorey) {
        log.info("Start TrainService::schemeSvg with schemeId {}, isFirstStorey {}", schemeId, isFirstStorey);

        var url = SVG_URL.newBuilder()
                .addPathSegment(String.valueOf(schemeId))
                .addPathSegment(isFirstStorey ? "PcFirstStorey" : "PcSecondStorey")
                .build();
        var httpRequest = new Request.Builder().url(url).get().build();
        try (var response = client.newCall(httpRequest).execute()) {
            var content = response.body() != null ? response.body().string() : "{}";
            switch (response.code()) {
                case 200 -> {
                    return content;
                }
                case 404 -> throw new ResultException("Not Found " + content);
                default -> throw new ResultException("Unexpected response" + content);
            }
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }

    @Retryable(
            retryFor = ReadException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100)
    )
    @Cacheable(RZD_CAR_SCHEME)
    public Schemes schemes(String carType) {
        log.info("Start TrainService::schemes with carType {}", carType);

        var url = CAR_SCHEME_URL.newBuilder()
                .addQueryParameter("CarType", carType)
                .build();
        var httpRequest = new Request.Builder().url(url).get().build();
        try (var response = client.newCall(httpRequest).execute()) {
            var content = response.body() != null ? response.body().string() : "{}";
            switch (response.code()) {
                case 200 -> {
                    return toSchemes(content);
                }
                case 404 -> throw new ResultException("Not Found " + content);
                default -> throw new ResultException("Unexpected response" + content);
            }
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }

    @Retryable(
            retryFor = ReadException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100)
    )
    @Cacheable(RZD_CAR_SVG)
    public String schemeSvg(String filename) {
        log.info("Start TrainService::filename with carType {}", filename);

        var url = SVG_SCHEME_URL.newBuilder()
                .addPathSegment(filename)
                .build();
        var httpRequest = new Request.Builder().url(url).get().build();
        try (var response = client.newCall(httpRequest).execute()) {
            var content = response.body() != null ? response.body().string() : "";
            switch (response.code()) {
                case 200 -> {
                    return content;
                }
                case 404 -> throw new ResultException("Not Found " + content);
                default -> throw new ResultException("Unexpected response" + content);
            }
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }

    private Schemes toSchemes(String content) {
        try {
            return objectMapper.readValue(content, Schemes.class);
        } catch (JsonProcessingException e) {
            throw new ParseException("Problem with parsing Json to ru.axenix.rzd.response.scheme.Schemes", e);
        }
    }


    private List<ResultDto> getResults(MmpRequest mmpRequest) {
        var body = RequestBody.create(toJson(mmpRequest), MEDIA_TYPE_APPLICATION_JSON);
        var httpRequest = new Request.Builder().url(ONEWAY_MMP_ROUTES_URL).post(body).build();
        try (var response = client.newCall(httpRequest).execute()) {
            var content = response.body() != null ? response.body().string() : "{}";
            switch (response.code()) {
                case 200 -> {
                    var responses = getResponses(content);
                    checkErrors(responses);
                    return toResults(responses);
                }
                case 400 -> throw new ResultException("Bad Request " + content);
                case 500 -> throw new ResultException("Internal Server Error " + content);
                default -> throw new ResultException("Unexpected response" + content);
            }
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }

    private RailwayV1SearchCarPricing getResults(CarPricingRequest request) {
        var body = RequestBody.create(toJson(request), MEDIA_TYPE_APPLICATION_JSON);
        var httpRequest = new Request.Builder().url(CAR_PRICING_URL).post(body).build();
        try (var response = client.newCall(httpRequest).execute()) {
            var content = response.body() != null ? response.body().string() : "{}";
            switch (response.code()) {
                case 200 -> {
                    return toCarPricing(content);
                }
                case 400 -> throw new ResultException("Bad Request %s %s".formatted(content, request));
                case 500 -> throw new ResultException("Internal Server Error %s %s".formatted(content, request));
                default -> throw new ResultException("Unexpected response %s %s".formatted(content, request));
            }
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }

    private RailwayV1SearchTrainPricing getResults(PRequest request) {
        var body = RequestBody.create(toJson(request), MEDIA_TYPE_APPLICATION_JSON);
        var httpRequest = new Request.Builder().url(TRAIN_PRICING_URL).post(body).build();
        try (var response = client.newCall(httpRequest).execute()) {
            var content = response.body() != null ? response.body().string() : "{}";
            switch (response.code()) {
                case 200 -> {
                    return toTrainPricing(content);
                }
                case 400 -> throw new ResultException("Bad Request %s %s".formatted(content, request));
                case 500 -> throw new ResultException("Internal Server Error %s %s".formatted(content, request));
                default -> throw new ResultException("Unexpected response %s %s".formatted(content, request));
            }
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }

    private RailwayV1SearchTrainPricing toTrainPricing(String line) {
        try {
            return objectMapper.readValue(line, RailwayV1SearchTrainPricing.class);
        } catch (JsonProcessingException e) {
            throw new ParseException("Problem with parsing Json to ru.axenix.rzd.response.result.RailwayV1SearchTrainPricing", e);
        }
    }

    private List<ResultDto> toResults(List<Response> responses) {
        return responses.stream()
                .filter(Result.class::isInstance)
                .map(Result.class::cast)
                .filter(result -> Objects.nonNull(result.getRoutes()))
                .map(result -> new ResultDto()
                        .setLegs(result.getLegs())
                        .setRoutes(result.getRoutes())
                        .setMaxPrice(result.getMaxPrice())
                        .setMinPrice(result.getMinPrice())
                ).toList();
    }

    private void checkErrors(List<Response> responses) {
        var errors = responses.stream()
                .filter(Error.class::isInstance)
                .map(Error.class::cast)
                .toList();

        if (!errors.isEmpty()) {
            log.warn("Errors found: {}", errors);
        }
    }

    private List<Response> getResponses(String content) {
        return Arrays.stream(content.split(LINE_BREAK))
                .map(this::toResponse)
                .toList();
    }

    private Response toResponse(String line) {
        try {
            return objectMapper.readValue(line, Response.class);
        } catch (JsonProcessingException e) {
            throw new ParseException("Problem with parsing Json to ru.axenix.rzd.response.Response", e);
        }
    }

    private RailwayV1SearchCarPricing toCarPricing(String line) {
        try {
            return objectMapper.readValue(line, RailwayV1SearchCarPricing.class);
        } catch (JsonProcessingException e) {
            throw new ParseException("Problem with parsing Json to ru.axenix.rzd.response.result.RailwayV1SearchCarPricing", e);
        }
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ParseException("Problem with parsing to Json", e);
        }
    }
}
