package ru.axenix.yandex.station;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Codes {
    @JsonProperty("yandex_code")
    private String yandexCode;
    @JsonProperty("iata")
    private String iata;
    @JsonProperty("nodeId")
    private String nodeId;
    @JsonProperty("express")
    private String express;
}
