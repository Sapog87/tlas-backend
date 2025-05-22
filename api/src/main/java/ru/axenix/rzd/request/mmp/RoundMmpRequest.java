package ru.axenix.rzd.request.mmp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString(callSuper = true)
public class RoundMmpRequest extends MmpRequest {
    @JsonProperty("backward_start_datetime_range")
    private StartDatetimeRange backwardStartDatetimeRange;
    @JsonProperty("forward_start_datetime_range")
    private StartDatetimeRange forwardStartDatetimeRange;
}
