package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FreePlacesByCompartment {
    @JsonProperty("CompartmentNumber")
    public Integer compartmentNumber;
    @JsonProperty("Places")
    public String places;
}
