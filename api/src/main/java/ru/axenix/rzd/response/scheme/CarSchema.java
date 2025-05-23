package ru.axenix.rzd.response.scheme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarSchema {
    @JsonProperty("CarSchemaId")
    private String carSchemaId;
    @JsonProperty("TrainNum")
    private String trainNum;
    @JsonProperty("CarType")
    private String carType;
    @JsonProperty("CarNum")
    private String carNum;
    @JsonProperty("ServiceClass")
    private String serviceClass;
    @JsonProperty("Content")
    private List<Content> content;
}