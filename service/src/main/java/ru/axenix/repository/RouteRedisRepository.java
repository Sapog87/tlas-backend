package ru.axenix.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;
import ru.axenix.dto.Route;

import java.time.Duration;
import java.util.List;

import static ru.axenix.config.RedisCacheConfig.FULL_ROUTE;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RouteRedisRepository {
    private final RedisTemplate<String, Route> redisTemplate;

    public void save(List<Route> routes) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (var route : routes) {
                var fullKey = createFullKey(route.getKey());
                var key = ((RedisSerializer<String>) redisTemplate.getKeySerializer()).serialize(fullKey);
                var value = ((RedisSerializer<Route>) redisTemplate.getValueSerializer()).serialize(route);
                connection.stringCommands().set(
                        key,
                        value,
                        Expiration.from(Duration.ofMinutes(30)),
                        RedisStringCommands.SetOption.UPSERT
                );
            }
            return null;
        });
    }

    public Route findByKey(String key) {
        return redisTemplate.opsForValue().get(createFullKey(key));
    }

    private String createFullKey(String key) {
        return "%s::%s".formatted(FULL_ROUTE, key);
    }
}
