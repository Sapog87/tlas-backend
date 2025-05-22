package ru.axenix.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Place {
    private Integer compartment;
    private String number;
    private String type;
    private Integer kopecks;
    private String serviceClass;
}
