package ru.axenix.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class Segment {
    private Transport transport;
    private String vehicle;
    private String raceNumber;
    private String carrier;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime startDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime finishDateTime;
    private String startStation;
    private String originCode;
    private String finishStation;
    private String destinationCode;
    private String startCity;
    private String finishCity;
    private List<Product> products;
}
