package ru.axenix.rzd.request.mmp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString(callSuper = true)
public class OneWayMmpRequest extends MmpRequest {
    @JsonProperty("start_datetime_range")
    private StartDatetimeRange startDatetimeRange;
}
