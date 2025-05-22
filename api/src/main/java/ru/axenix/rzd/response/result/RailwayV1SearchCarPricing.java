package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RailwayV1SearchCarPricing {
    @JsonProperty("OriginCode")
    private String originCode;
    @JsonProperty("DestinationCode")
    private String destinationCode;
    @JsonProperty("DestinationTimeZoneDifference")
    private Integer destinationTimeZoneDifference;
    @JsonProperty("OriginTimeZoneDifference")
    private Integer originTimeZoneDifference;
    @JsonProperty("TrainInfo")
    private Train train;
    @JsonProperty("Cars")
    private List<Car> cars;
}
