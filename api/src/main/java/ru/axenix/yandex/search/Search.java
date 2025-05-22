package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Search {
    @JsonProperty("to")
    private Location to;
    @JsonProperty("from")
    private Location from;
    @JsonProperty("date")
    private LocalDate date;
}
