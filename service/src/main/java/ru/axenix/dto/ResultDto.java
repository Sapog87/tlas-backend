package ru.axenix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.axenix.rzd.response.result.Leg;
import ru.axenix.rzd.response.result.Price;
import ru.axenix.rzd.response.result.Route;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class ResultDto {
    @JsonProperty("routes")
    private List<Route> routes;
    @JsonProperty("legs")
    private List<Leg> legs;
    @JsonProperty("min_price")
    private Price minPrice;
    @JsonProperty("max_price")
    private Price maxPrice;
}
