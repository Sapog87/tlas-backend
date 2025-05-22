package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationInfo {
    @JsonProperty("IsoCode")
    private String isoCode;
    @JsonProperty("RegionName")
    private String regionName;
    @JsonProperty("StationCode")
    private String stationCode;
    @JsonProperty("StationName")
    private String stationName;
}
