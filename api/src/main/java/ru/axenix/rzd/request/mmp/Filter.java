package ru.axenix.rzd.request.mmp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Filter {
    @JsonProperty("param_name")
    private String paramName;
    @JsonProperty("exact_filter")
    private ExactFilter exactFilter;
}
