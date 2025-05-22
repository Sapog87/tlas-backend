package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {
    @JsonProperty("DestinationStationCode")
    public String destinationStationCode;
    @JsonProperty("CarType")
    public String carType;
    @JsonProperty("RailwayCarSchemeId")
    public Integer railwayCarSchemeId;
    @JsonProperty("CarSubType")
    public String carSubType;
    @JsonProperty("CarTypeName")
    public String carTypeName;
    @JsonProperty("CarSchemeName")
    public String carSchemeName;
    @JsonProperty("CarNumber")
    public String carNumber;
    @JsonProperty("ServiceClass")
    public String serviceClass;
    @JsonProperty("FreePlaces")
    public String freePlaces;
    @JsonProperty("FreePlacesByCompartments")
    public List<FreePlacesByCompartment> freePlacesByCompartments;
    @JsonProperty("PlaceQuantity")
    public Integer placeQuantity;
    @JsonProperty("IsTwoStorey")
    public Boolean isTwoStorey;
    @JsonProperty("Services")
    public List<String> services;
    @JsonProperty("MinPrice")
    public Double minPrice;
    @JsonProperty("MaxPrice")
    public Double maxPrice;
    @JsonProperty("ServiceCost")
    public Double serviceCost;
    @JsonProperty("PlaceReservationType")
    public String placeReservationType;
    @JsonProperty("Carrier")
    public String carrier;
    @JsonProperty("CarrierDisplayName")
    public String carrierDisplayName;
    @JsonProperty("TrainNumber")
    public String trainNumber;
    @JsonProperty("ArrivalDateTime")
    public LocalDateTime arrivalDateTime;
    @JsonProperty("LocalArrivalDateTime")
    public LocalDateTime localArrivalDateTime;
//    @JsonProperty("HasNoInterchange")
//    public boolean hasNoInterchange;
//    @JsonProperty("HasPlaceNumeration")
//    public boolean hasPlaceNumeration;
//    @JsonProperty("IsBeddingSelectionPossible")
//    public boolean isBeddingSelectionPossible;
//    @JsonProperty("HasElectronicRegistration")
//    public boolean hasElectronicRegistration;
//    @JsonProperty("HasDynamicPricing")
//    public boolean hasDynamicPricing;
//    @JsonProperty("HasPlacesNearBabies")
//    public boolean hasPlacesNearBabies;
//    @JsonProperty("HasPlacesNearPlayground")
//    public boolean hasPlacesNearPlayground;
//    @JsonProperty("HasPlacesNearPets")
//    public boolean hasPlacesNearPets;
//    @JsonProperty("HasNonRefundableTariff")
//    public boolean hasNonRefundableTariff;
//    @JsonProperty("OnlyNonRefundableTariff")
//    public boolean onlyNonRefundableTariff;
//    @JsonProperty("IsAdditionalPassengerAllowed")
//    public boolean isAdditionalPassengerAllowed;
//    @JsonProperty("IsChildTariffTypeAllowed")
//    public boolean isChildTariffTypeAllowed;
    @JsonProperty("CarPlaceType")
    public String carPlaceType;
//    @JsonProperty("CarPlaceCode")
//    public String carPlaceCode;
//    @JsonProperty("CarPlaceNameRu")
//    public String carPlaceNameRu;
//    @JsonProperty("CarPlaceNameEn")
//    public String carPlaceNameEn;
//    @JsonProperty("Discounts")
//    public ArrayList<Discount> discounts;
//    @JsonProperty("IsSaleForbidden")
//    public boolean isSaleForbidden;
//    @JsonProperty("AvailabilityIndication")
//    public String availabilityIndication;
//    @JsonProperty("IsThreeHoursReservationAvailable")
//    public boolean isThreeHoursReservationAvailable;
//    @JsonProperty("Road")
//    public String road;
//    @JsonProperty("InfoRequestSchema")
//    public String infoRequestSchema;
//    @JsonProperty("PassengerSpecifyingRules")
//    public String passengerSpecifyingRules;
//    @JsonProperty("IsMealOptionPossible")
//    public boolean isMealOptionPossible;
//    @JsonProperty("IsAdditionalMealOptionPossible")
//    public boolean isAdditionalMealOptionPossible;
//    @JsonProperty("IsOnRequestMealOptionPossible")
//    public boolean isOnRequestMealOptionPossible;
//    @JsonProperty("MealSalesOpenedTill")
//    public LocalDateTime mealSalesOpenedTill;
//    @JsonProperty("IsTransitDocumentRequired")
//    public boolean isTransitDocumentRequired;
//    @JsonProperty("IsInterstate")
//    public boolean isInterstate;
//    @JsonProperty("ClientFeeCalculation")
//    public Object clientFeeCalculation;
//    @JsonProperty("AgentFeeCalculation")
//    public Object agentFeeCalculation;
//    @JsonProperty("IsBranded")
//    public boolean isBranded;
//    @JsonProperty("IsBuffet")
//    public boolean isBuffet;
//    @JsonProperty("TripDirection")
//    public String tripDirection;
//    @JsonProperty("IsFromUkrainianCalcCenter")
//    public boolean isFromUkrainianCalcCenter;
//    @JsonProperty("IsForDisabledPersons")
//    public boolean isForDisabledPersons;
//    @JsonProperty("IsSpecialSaleMode")
//    public boolean isSpecialSaleMode;
//    @JsonProperty("BoardingSystemType")
//    public String boardingSystemType;
//    @JsonProperty("AvailableBaggageTypes")
//    public List<AvailableBaggageType> availableBaggageTypes;
//    @JsonProperty("IsTourPackageAvailable")
//    public boolean isTourPackageAvailable;
//    @JsonProperty("ArePlacesForBusinessTravelBooking")
//    public boolean arePlacesForBusinessTravelBooking;
//    @JsonProperty("IsCarTransportationCoach")
//    public boolean isCarTransportationCoach;
//    @JsonProperty("CarNumeration")
//    public String carNumeration;
//    @JsonProperty("IsGroupTransportaionAvailable")
//    public boolean isGroupTransportaionAvailable;
//    @JsonProperty("IsAutoBookingAvailable")
//    public boolean isAutoBookingAvailable;
//    @JsonProperty("TrainNumberFromFormingStation")
//    public String trainNumberFromFormingStation;
//    @JsonProperty("PlacesWithConditionalRefundableTariffQuantity")
//    public int placesWithConditionalRefundableTariffQuantity;
//    @JsonProperty("CarPlaceName")
//    public String carPlaceName;
//    @JsonProperty("HasFssBenefit")
//    public boolean hasFssBenefit;
//    @JsonProperty("ServiceClassName")
//    public String serviceClassName;
}
