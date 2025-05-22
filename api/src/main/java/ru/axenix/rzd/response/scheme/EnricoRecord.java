package ru.axenix.rzd.response.scheme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnricoRecord {
    @JsonProperty("schemaPC1Horizontal")
    private String schemaPC1Horizontal;
    @JsonProperty("schemaPC2Horizontal")
    private String schemaPC2Horizontal;
    @JsonProperty("schemaMob1Horizontal")
    private String schemaMob1Horizontal;
    @JsonProperty("schemaMob2Horizontal")
    private String schemaMob2Horizontal;
    @JsonProperty("schemaMob1Vertical")
    private String schemaMob1Vertical;
    @JsonProperty("schemaMob2Vertical")
    private String schemaMob2Vertical;
}
