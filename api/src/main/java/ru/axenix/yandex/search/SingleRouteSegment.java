package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleRouteSegment extends Segment {
    @JsonProperty("thread")
    private Thread thread;
    @JsonProperty("from")
    private Location from;
    @JsonProperty("to")
    private Location to;
}
