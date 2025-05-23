package ru.axenix.rzd.response.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trip {
    @JsonProperty("start_location")
    private Location startLocation;
    @JsonProperty("finish_location")
    private Location finishLocation;
    @JsonProperty("start_datetime")
    private OffsetDateTime startDatetime;
    @JsonProperty("finish_datetime")
    private OffsetDateTime finishDatetime;
    @JsonProperty("race_number")
    private String raceNumber;
    //    @JsonProperty("min_price")
//    private Price minPrice;
//    @JsonProperty("max_price")
//    private Price maxPrice;
//    @JsonProperty("free_places")
//    private int freePlaces;
    @JsonProperty("raw_data")
    private RawData rawData;
    @JsonProperty("products")
    private List<Product> products;
//    @JsonProperty("display_priority")
//    private String displayPriority;
//    @JsonProperty("legality")
//    private String legality;
}
