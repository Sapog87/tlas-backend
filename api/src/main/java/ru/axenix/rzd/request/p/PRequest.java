package ru.axenix.rzd.request.p;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PRequest {
    @JsonProperty("origin")
    private String origin;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("departureDate")
    private LocalDateTime departureDate;
    @JsonProperty("carGrouping")
    private String carGrouping;
}
