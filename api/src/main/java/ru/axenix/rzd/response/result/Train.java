package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {
    @JsonProperty("CarGroups")
    public List<CarGroup> carGroups;
    @JsonProperty("IsFromSchedule")
    public Boolean isFromSchedule;
    @JsonProperty("IsTourPackagePossible")
    public Boolean isTourPackagePossible;
    @JsonProperty("CategoryId")
    public Integer categoryId;
    @JsonProperty("ScheduleId")
    public Integer scheduleId;
    @JsonProperty("Provider")
    public String provider;
    @JsonProperty("IsWaitListAvailable")
    public Boolean isWaitListAvailable;
    @JsonProperty("HasElectronicRegistration")
    public Boolean hasElectronicRegistration;
    @JsonProperty("HasCarTransportationCoaches")
    public Boolean hasCarTransportationCoaches;
    @JsonProperty("HasDynamicPricingCars")
    public Boolean hasDynamicPricingCars;
    @JsonProperty("HasTwoStoreyCars")
    public Boolean hasTwoStoreyCars;
    @JsonProperty("HasSpecialSaleMode")
    public Boolean hasSpecialSaleMode;
    @JsonProperty("Carriers")
    public List<String> carriers;
    @JsonProperty("CarrierDisplayNames")
    public List<String> carrierDisplayNames;
    @JsonProperty("Id")
    public Integer id;
    @JsonProperty("IsBranded")
    public Boolean isBranded;
    @JsonProperty("TrainNumber")
    public String trainNumber;
    @JsonProperty("TrainNumberToGetRoute")
    public String trainNumberToGetRoute;
    @JsonProperty("DisplayTrainNumber")
    public String displayTrainNumber;
    @JsonProperty("TrainDescription")
    public String trainDescription;
    @JsonProperty("TrainName")
    public String trainName;
    @JsonProperty("TrainNameEn")
    public String trainNameEn;
    @JsonProperty("TransportType")
    public String transportType;
    @JsonProperty("OriginName")
    public String originName;
    @JsonProperty("InitialStationName")
    public String initialStationName;
    @JsonProperty("OriginStationCode")
    public String originStationCode;
    @JsonProperty("OriginStationInfo")
    public StationInfo originStationInfo;
    @JsonProperty("InitialTrainStationInfo")
    public StationInfo initialTrainStationInfo;
    @JsonProperty("InitialTrainStationCode")
    public String initialTrainStationCode;
    @JsonProperty("InitialTrainStationCnsiCode")
    public String initialTrainStationCnsiCode;
    @JsonProperty("DestinationName")
    public String destinationName;
    @JsonProperty("FinalStationName")
    public String finalStationName;
    @JsonProperty("DestinationStationCode")
    public String destinationStationCode;
    @JsonProperty("DestinationStationInfo")
    public StationInfo destinationStationInfo;
    @JsonProperty("FinalTrainStationInfo")
    public StationInfo finalTrainStationInfo;
    @JsonProperty("FinalTrainStationCode")
    public String finalTrainStationCode;
    @JsonProperty("FinalTrainStationCnsiCode")
    public String finalTrainStationCnsiCode;
    @JsonProperty("DestinationNames")
    public List<String> destinationNames;
    @JsonProperty("FinalStationNames")
    public List<String> finalStationNames;
    @JsonProperty("DepartureDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime departureDateTime;
    @JsonProperty("LocalDepartureDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime localDepartureDateTime;
    @JsonProperty("ArrivalDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime arrivalDateTime;
    @JsonProperty("LocalArrivalDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime localArrivalDateTime;
    @JsonProperty("ArrivalDateTimes")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public List<LocalDateTime> arrivalDateTimes;
    @JsonProperty("LocalArrivalDateTimes")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public List<LocalDateTime> localArrivalDateTimes;
    @JsonProperty("DepartureDateFromFormingStation")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime departureDateFromFormingStation;
    @JsonProperty("DepartureStopTime")
    public Integer departureStopTime;
    @JsonProperty("ArrivalStopTime")
    public Integer arrivalStopTime;
    @JsonProperty("TripDuration")
    public Double tripDuration;
    @JsonProperty("TripDistance")
    public Integer tripDistance;
    @JsonProperty("IsSuburban")
    public Boolean isSuburban;
    @JsonProperty("IsComponent")
    public Boolean isComponent;
    @JsonProperty("IsSaleForbidden")
    public Boolean isSaleForbidden;
    @JsonProperty("IsTicketPrintRequiredForBoarding")
    public Boolean isTicketPrintRequiredForBoarding;
    @JsonProperty("BookingSystem")
    public String bookingSystem;
    @JsonProperty("IsVrStorageSystem")
    public Boolean isVrStorageSystem;
    @JsonProperty("PlacesStorageType")
    public String placesStorageType;
    @JsonProperty("BoardingSystemTypes")
    public List<String> boardingSystemTypes;
    @JsonProperty("TrainBrandCode")
    public String trainBrandCode;
    @JsonProperty("ServiceProvider")
    public String serviceProvider;
    @JsonProperty("DestinationStationName")
    public String destinationStationName;
    @JsonProperty("OriginStationName")
    public String originStationName;
    @JsonProperty("IsPlaceRangeAllowed")
    public Boolean isPlaceRangeAllowed;
    @JsonProperty("IsTrainRouteAllowed")
    public Boolean isTrainRouteAllowed;
}
