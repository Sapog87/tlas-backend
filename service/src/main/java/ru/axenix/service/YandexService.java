package ru.axenix.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.axenix.exception.ReadException;
import ru.axenix.exception.ResultException;
import ru.axenix.yandex.search.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static ru.axenix.config.RedisCacheConfig.YANDEX_RESPONSE;

@Slf4j
@Service
public class YandexService {
    private static final String YANDEX_RASP_API_URL = "https://api.rasp.yandex.net/v3.0/search";

    private final ObjectMapper objectMapper;
    private final OkHttpClient client;
    private final HttpUrl baseUrl;

    public YandexService(
            ObjectMapper objectMapper,
            OkHttpClient client,
            @Value("${yandex.rasp.api.key}") String apiKey
    ) {
        this.objectMapper = objectMapper;
        this.client = client;
        this.baseUrl = Objects.requireNonNull(HttpUrl.parse(YANDEX_RASP_API_URL))
                .newBuilder()
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("show_systems", "all")
                .addQueryParameter("transport_types", "all")
                .addQueryParameter("transfers", "true")
                .build();
    }

    @Retryable(
            retryFor = ReadException.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 100)
    )
    @Cacheable(YANDEX_RESPONSE)
    public Response getResult(String from, String to, LocalDate date) {
        var url = baseUrl.newBuilder()
                .addQueryParameter("from", from)
                .addQueryParameter("to", to)
                .addQueryParameter("date", date.toString())
                .build();

        var httpRequest = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (var response = client.newCall(httpRequest).execute()) {
            var content = response.body() != null ? response.body().string() : "";
            switch (response.code()) {
                case 200 -> {
                    return objectMapper.readValue(content, Response.class);
                }
                case 400 -> throw new ResultException("Bad Request %s %s".formatted(content, url));
                case 500 -> throw new ResultException("Internal Server Error %s %s".formatted(content, url));
                default -> throw new ResultException("Unexpected response %s %s".formatted(content, url));
            }
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }
}
