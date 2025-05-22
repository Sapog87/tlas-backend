package ru.axenix.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TrainProduct extends Product {
    private CarType carType;
    private Integer priceInKopecks;
    private Integer freePlaces;
}
