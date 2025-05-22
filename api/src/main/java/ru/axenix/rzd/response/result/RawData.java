package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawData {
    @JsonProperty("/Railway/V1/Search/TrainPricing")
    private RailwayV1SearchTrainPricing pricing;
}
