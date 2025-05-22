package ru.axenix.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SchemeDto {
    private String firstStorey;
    private String secondStorey;
}
