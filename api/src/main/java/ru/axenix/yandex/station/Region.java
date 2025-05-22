package ru.axenix.yandex.station;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Region {
    @JsonProperty("title")
    private String title;
    @JsonProperty("codes")
    private Codes codes;
    @JsonProperty("settlements")
    private List<Settlement> settlements;
}
