package ru.axenix.rzd.request.mmp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SystemParams {
    @JsonProperty("search_via_ar")
    private String searchViaAr;
    @JsonProperty("search_via_graph")
    private String searchViaGraph;
    @JsonProperty("detailed_location")
    private boolean detailedLocation;
    @JsonProperty("debug")
    private boolean debug;
}
