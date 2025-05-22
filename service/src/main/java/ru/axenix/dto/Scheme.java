package ru.axenix.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Scheme {
    private Integer id;
    private String name;
    private Boolean isFirstStorey;
    private String svg;
}
