package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class Codes {
    @JsonProperty("yandex")
    private String yandex;
    @JsonProperty("iata")
    private String iata;
    @JsonProperty("express")
    private String express;
}
