package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.axenix.rzd.response.Response;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("result")
public class Result extends Response {
    @JsonProperty("routes")
    private List<Route> routes;
    @JsonProperty("legs")
    private List<Leg> legs;
    @JsonProperty("min_price")
    private Price minPrice;
    @JsonProperty("max_price")
    private Price maxPrice;
}
