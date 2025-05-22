package ru.axenix.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Coach {
    private Integer schemeId;
    private String schemeName;
    private String number;
    private CarType carType;
    private List<Place> freePlaces;
    private Boolean isTwoStorey;
}
