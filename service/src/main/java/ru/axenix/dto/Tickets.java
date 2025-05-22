package ru.axenix.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Tickets {
    private String vehicle;
    private String raceNumber;
    private List<Coach> coaches;
    private List<Scheme> schemes;
}
