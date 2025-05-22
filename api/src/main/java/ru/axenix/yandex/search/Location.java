package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    @JsonProperty("type")
    private String type;
    @JsonProperty("title")
    private String title;
    @JsonProperty("code")
    private String code;
    @JsonProperty("codes")
    private Codes codes;
    @JsonProperty("transport_type")
    private String transportType;
}
