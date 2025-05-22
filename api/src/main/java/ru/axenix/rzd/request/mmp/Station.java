package ru.axenix.rzd.request.mmp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Station {
    @JsonProperty("key")
    private String key;
}
