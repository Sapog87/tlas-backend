package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @JsonProperty("price")
    private Price price;
    @JsonProperty("free_places")
    private int freePlaces;
    @JsonProperty("train_car_type")
    private TrainCarType trainCarType;
}
