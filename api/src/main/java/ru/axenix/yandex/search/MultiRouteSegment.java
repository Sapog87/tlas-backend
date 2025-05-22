package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiRouteSegment extends Segment {
    @JsonProperty("transport_types")
    private List<String> transportTypes;
    @JsonProperty("departure_from")
    private Location departureFrom;
    @JsonProperty("arrival_to")
    private Location arrivalTo;
    @JsonProperty("details")
    private List<Detail> segments;
}
