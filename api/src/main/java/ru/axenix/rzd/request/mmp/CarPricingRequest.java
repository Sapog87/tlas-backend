package ru.axenix.rzd.request.mmp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Builder
@Getter
@ToString
public class CarPricingRequest {
    @JsonProperty("OriginCode")
    private String originCode;
    @JsonProperty("DestinationCode")
    private String destinationCode;
    @JsonProperty("DepartureDate")
    private OffsetDateTime departureTime;
    @JsonProperty("TrainNumber")
    private String trainNumber;
}
