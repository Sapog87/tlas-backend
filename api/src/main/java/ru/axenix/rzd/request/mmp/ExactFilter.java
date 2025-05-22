package ru.axenix.rzd.request.mmp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExactFilter {
    @JsonProperty("param_values")
    private List<String> paramValues;
}
