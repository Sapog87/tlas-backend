package ru.axenix.yandex.station;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {
    @JsonProperty("title")
    private String title;
    @JsonProperty("codes")
    private Codes codes;
    @JsonProperty("station_type")
    private String stationType;
    @JsonProperty("transport_type")
    private String transportType;
}
