package ru.axenix.rzd.response.scheme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {
    @JsonProperty("enrico_type")
    private String enricoType;
    @JsonProperty("enrico_record")
    private EnricoRecord enricoRecord;
}
