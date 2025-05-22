package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    @JsonProperty("provider")
    private Provider provider;
    @JsonProperty("min_price")
    private Price minPrice;
    @JsonProperty("max_price")
    private Price maxPrice;
    @JsonProperty("free_places")
    private int freePlaces;
    @JsonProperty("segments")
    private List<Segment> segments;
}
