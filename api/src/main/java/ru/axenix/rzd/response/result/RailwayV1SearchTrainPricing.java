package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RailwayV1SearchTrainPricing {
    @JsonProperty("OriginStationCode")
    public String originStationCode;
    @JsonProperty("DestinationStationCode")
    public String destinationStationCode;
//    @JsonProperty("ClientFeeCalculation")
//    public Object clientFeeCalculation;
//    @JsonProperty("AgentFeeCalculation")
//    public Object agentFeeCalculation;
    @JsonProperty("OriginCode")
    public String originCode;
    @JsonProperty("OriginStationInfo")
    public StationInfo originStationInfo;
    @JsonProperty("OriginTimeZoneDifference")
    public int originTimeZoneDifference;
    @JsonProperty("DestinationCode")
    public String destinationCode;
    @JsonProperty("DestinationStationInfo")
    public StationInfo destinationStationInfo;
    @JsonProperty("DestinationTimeZoneDifference")
    public int destinationTimeZoneDifference;
    @JsonProperty("RoutePolicy")
    public String routePolicy;
    @JsonProperty("DepartureTimeDescription")
    public String departureTimeDescription;
    @JsonProperty("ArrivalTimeDescription")
    public String arrivalTimeDescription;
    @JsonProperty("IsFromUkrain")
    public boolean isFromUkrain;
    @JsonProperty("NotAllTrainsReturned")
    public boolean notAllTrainsReturned;
    @JsonProperty("BookingSystem")
    public String bookingSystem;
    @JsonProperty("Id")
    public int id;
    @JsonProperty("DestinationStationName")
    public String destinationStationName;
    @JsonProperty("OriginStationName")
    public String originStationName;
    @JsonProperty("MoscowDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    public LocalDateTime moscowDateTime;
    @JsonProperty("Trains")
    private List<Train> trains;
}
