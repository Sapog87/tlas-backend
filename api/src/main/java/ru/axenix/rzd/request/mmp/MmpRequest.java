package ru.axenix.rzd.request.mmp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@ToString
public abstract class MmpRequest {
    @JsonProperty("max_results")
    private int maxResults;
    @JsonProperty("min_trips_in_leg")
    private int minTripsInLeg;
    @JsonProperty("max_trips_in_leg")
    private int maxTripsInLeg;
    @JsonProperty("system_params")
    private SystemParams systemParams;
    @JsonProperty("start_location")
    private Location startLocation;
    @JsonProperty("finish_location")
    private Location finishLocation;
    @JsonProperty("filters")
    private List<Filter> filters;
}
