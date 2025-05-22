package ru.axenix.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.axenix.dto.Route;
import ru.axenix.rzd.response.result.RailwayV1SearchCarPricing;
import ru.axenix.rzd.response.scheme.Schemes;

import java.util.List;

import static java.time.Duration.ofMinutes;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@EnableCaching
@Configuration
public class RedisCacheConfig {
    public static final String RZD_ROUTE = "rzdRoute";
    public static final String YANDEX_ROUTE = "yandexRoute";
    public static final String RZD_CAR_PRICING = "rzdCarPricing";
    public static final String RZD_CAR_SVG = "rzdCarSvg";
    public static final String RZD_CAR_SCHEME = "rzdCarScheme";

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory,
            ObjectMapper objectMapper
    ) {
        return fromConnectionFactory(redisConnectionFactory)
                .withCacheConfiguration(RZD_ROUTE, defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(fromSerializer(
                                new Jackson2JsonRedisSerializer<>(
                                        objectMapper,
                                        objectMapper
                                                .getTypeFactory()
                                                .constructCollectionType(List.class, Route.class)
                                )
                        ))
                        .entryTtl(ofMinutes(3)))
                .withCacheConfiguration(YANDEX_ROUTE, defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(fromSerializer(
                                new Jackson2JsonRedisSerializer<>(
                                        objectMapper,
                                        objectMapper
                                                .getTypeFactory()
                                                .constructCollectionType(List.class, Route.class)
                                )
                        ))
                        .entryTtl(ofMinutes(3)))
                .withCacheConfiguration(RZD_CAR_PRICING, defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(fromSerializer(
                                new Jackson2JsonRedisSerializer<>(
                                        objectMapper,
                                        RailwayV1SearchCarPricing.class
                                )
                        ))
                        .entryTtl(ofMinutes(3)))
                .withCacheConfiguration(RZD_CAR_SCHEME, defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(fromSerializer(
                                new Jackson2JsonRedisSerializer<>(
                                        objectMapper,
                                        Schemes.class
                                )
                        ))
                        .entryTtl(ofMinutes(3)))
                .withCacheConfiguration(RZD_CAR_SVG, defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(fromSerializer(new StringRedisSerializer()))
                )
                .build();
    }
}
