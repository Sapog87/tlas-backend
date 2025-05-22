package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Leg {
    @JsonProperty("start_location")
    private Location startLocation;
    @JsonProperty("finish_location")
    private Location finishLocation;
    @JsonProperty("start_datetime")
    private OffsetDateTime startDatetime;
    @JsonProperty("finish_datetime")
    private OffsetDateTime finishDatetime;
}
