package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarGroup {
    @JsonProperty("Carriers")
    public List<String> carriers;
    @JsonProperty("CarrierDisplayNames")
    public List<String> carrierDisplayNames;
    @JsonProperty("ServiceClasses")
    public List<String> serviceClasses;
    @JsonProperty("MinPrice")
    public double minPrice;
    @JsonProperty("MaxPrice")
    public double maxPrice;
    @JsonProperty("CarType")
    public String carType;
    @JsonProperty("CarTypeName")
    public String carTypeName;
    @JsonProperty("PlaceQuantity")
    public int placeQuantity;
    @JsonProperty("LowerPlaceQuantity")
    public int lowerPlaceQuantity;
    @JsonProperty("UpperPlaceQuantity")
    public int upperPlaceQuantity;
    @JsonProperty("LowerSidePlaceQuantity")
    public int lowerSidePlaceQuantity;
    @JsonProperty("UpperSidePlaceQuantity")
    public int upperSidePlaceQuantity;
    @JsonProperty("PlacesWithConditionalRefundableTariffQuantity")
    public int placesWithConditionalRefundableTariffQuantity;
    @JsonProperty("LowerPlacesWithConditionalRefundableTariffQuantity")
    public int lowerPlacesWithConditionalRefundableTariffQuantity;
    @JsonProperty("UpperPlacesWithConditionalRefundableTariffQuantity")
    public int upperPlacesWithConditionalRefundableTariffQuantity;
    @JsonProperty("MalePlaceQuantity")
    public int malePlaceQuantity;
    @JsonProperty("FemalePlaceQuantity")
    public int femalePlaceQuantity;
    @JsonProperty("EmptyCabinQuantity")
    public int emptyCabinQuantity;
    @JsonProperty("MixedCabinQuantity")
    public int mixedCabinQuantity;
    @JsonProperty("IsSaleForbidden")
    public boolean isSaleForbidden;
    @JsonProperty("AvailabilityIndication")
    public String availabilityIndication;
    @JsonProperty("CarDescriptions")
    public List<String> carDescriptions;
    //    @JsonProperty("ServiceClassNameRu")
//    public Object serviceClassNameRu;
//    @JsonProperty("ServiceClassNameEn")
//    public Object serviceClassNameEn;
    @JsonProperty("InternationalServiceClasses")
    public List<String> internationalServiceClasses;
    @JsonProperty("ServiceCosts")
    public List<Double> serviceCosts;
    @JsonProperty("IsBeddingSelectionPossible")
    public boolean isBeddingSelectionPossible;
    @JsonProperty("BoardingSystemTypes")
    public List<String> boardingSystemTypes;
    @JsonProperty("HasElectronicRegistration")
    public boolean hasElectronicRegistration;
    @JsonProperty("HasGenderCabins")
    public boolean hasGenderCabins;
    @JsonProperty("HasPlaceNumeration")
    public boolean hasPlaceNumeration;
    @JsonProperty("HasPlacesNearPlayground")
    public boolean hasPlacesNearPlayground;
    @JsonProperty("HasPlacesNearPets")
    public boolean hasPlacesNearPets;
    @JsonProperty("HasPlacesForDisabledPersons")
    public boolean hasPlacesForDisabledPersons;
    @JsonProperty("HasPlacesNearBabies")
    public boolean hasPlacesNearBabies;
    //    @JsonProperty("AvailableBaggageTypes")
//    public List<AvailableBaggageType> availableBaggageTypes;
    @JsonProperty("HasNonRefundableTariff")
    public boolean hasNonRefundableTariff;
    //    @JsonProperty("Discounts")
//    public List<Discount> discounts;
//    @JsonProperty("AllowedTariffs")
//    public List<Object> allowedTariffs;
    @JsonProperty("InfoRequestSchema")
    public String infoRequestSchema;
    @JsonProperty("TotalPlaceQuantity")
    public int totalPlaceQuantity;
    @JsonProperty("PlaceReservationTypes")
    public List<String> placeReservationTypes;
    @JsonProperty("IsThreeHoursReservationAvailable")
    public boolean isThreeHoursReservationAvailable;
    @JsonProperty("IsMealOptionPossible")
    public boolean isMealOptionPossible;
    @JsonProperty("IsAdditionalMealOptionPossible")
    public boolean isAdditionalMealOptionPossible;
    @JsonProperty("IsOnRequestMealOptionPossible")
    public boolean isOnRequestMealOptionPossible;
    @JsonProperty("IsTransitDocumentRequired")
    public boolean isTransitDocumentRequired;
    @JsonProperty("IsInterstate")
    public boolean isInterstate;
    //    @JsonProperty("ClientFeeCalculation")
//    public Object clientFeeCalculation;
//    @JsonProperty("AgentFeeCalculation")
//    public Object agentFeeCalculation;
    @JsonProperty("HasNonBrandedCars")
    public boolean hasNonBrandedCars;
    @JsonProperty("TripPointQuantity")
    public int tripPointQuantity;
    @JsonProperty("HasPlacesForBusinessTravelBooking")
    public boolean hasPlacesForBusinessTravelBooking;
    @JsonProperty("IsCarTransportationCoaches")
    public boolean isCarTransportationCoaches;
    //    @JsonProperty("IsGroupTransportaionAvailable")
//    public Object isGroupTransportaionAvailable;
    @JsonProperty("ServiceClassName")
    public String serviceClassName;
    @JsonProperty("HasFssBenefit")
    public boolean hasFssBenefit;
}
