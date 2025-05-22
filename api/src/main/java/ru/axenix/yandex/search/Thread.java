package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class Thread {
    @JsonProperty("number")
    private String number;
    @JsonProperty("title")
    private String title;
    @JsonProperty("short_title")
    private String shortTitle;
    @JsonProperty("carrier")
    private Carrier carrier;
    @JsonProperty("vehicle")
    private String vehicle;
    @JsonProperty("transport_type")
    private String transportType;
    @JsonProperty("transport_subtype")
    private TransportSubtype transportSubtype;
}
