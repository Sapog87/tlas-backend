package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfer extends Detail {
    @JsonProperty("transfer_point")
    private Location transferPoint;
    @JsonProperty("transfer_from")
    private Location transferFrom;
    @JsonProperty("transfer_to")
    private Location transferTo;
}
