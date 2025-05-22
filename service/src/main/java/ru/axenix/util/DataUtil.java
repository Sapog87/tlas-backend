package ru.axenix.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.axenix.yandex.station.Region;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataUtil {
    private final ObjectMapper mapper;

    @SneakyThrows
    public List<Region> getRegions() {
        var res = new ClassPathResource("response.json");
        try (var is = res.getInputStream()) {
            return mapper.readValue(is, new TypeReference<>() {
            });
        }
    }
}
