package ru.axenix.config;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {
    public static final MediaType MEDIA_TYPE_APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
