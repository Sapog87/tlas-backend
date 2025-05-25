package ru.axenix.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.axenix.dto.ResultDto;
import ru.axenix.dto.Route;
import ru.axenix.rzd.response.result.RailwayV1SearchCarPricing;
import ru.axenix.rzd.response.result.RailwayV1SearchTrainPricing;
import ru.axenix.rzd.response.result.Result;
import ru.axenix.rzd.response.scheme.Schemes;
import ru.axenix.yandex.search.Response;

import java.util.List;

import static java.time.Duration.ofMinutes;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@EnableCaching
@Configuration
public class RedisCacheConfig {
    public static final String RZD_ROUTE_MMP = "rzdRouteMmp";
    public static final String RZD_ROUTE_P = "rzdRouteP";
    public static final String YANDEX_RESPONSE = "yandexResponse";
    public static final String RZD_CAR_PRICING = "rzdCarPricing";
    public static final String RZD_CAR_SVG = "rzdCarSvg";
    public static final String RZD_CAR_SCHEME = "rzdCarScheme";
    public static final String FULL_ROUTE = "fullRoute";

    @Bean
    public StringRedisSerializer getStringSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public Jackson2JsonRedisSerializer<Route> getJackson2JsonRedisRouteSerializer(ObjectMapper objectMapper) {
        return new Jackson2JsonRedisSerializer<>(objectMapper, Route.class);
    }

    @Bean
    public RedisTemplate<String, Route> redisTemplate(
            RedisConnectionFactory connectionFactory,
            StringRedisSerializer stringSerializer,
            Jackson2JsonRedisSerializer<Route> jackson2JsonRedisSerializer
    ) {
        var template = new RedisTemplate<String, Route>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory,
            ObjectMapper objectMapper
    ) {
        var mapper = objectMapper.copy();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return fromConnectionFactory(redisConnectionFactory)
                .withCacheConfiguration(RZD_ROUTE_MMP, defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(fromSerializer(
                                new Jackson2JsonRedisSerializer<>(
                                        objectMapper,
                                        objectMapper
                                                .getTypeFactory()
                                                .constructCollectionType(List.class, ResultDto.class)
                                )
                        ))
                        .entryTtl(ofMinutes(3)))
                .withCacheConfiguration(RZD_ROUTE_P, defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(fromSerializer(
                                new Jackson2JsonRedisSerializer<>(
                                        objectMapper,
                                        RailwayV1SearchTrainPricing.class
                                )
                        ))
                        .entryTtl(ofMinutes(3)))
                .withCacheConfiguration(YANDEX_RESPONSE, defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeValuesWith(fromSerializer(
                                new Jackson2JsonRedisSerializer<>(
                                        objectMapper,
                                        Response.class
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
