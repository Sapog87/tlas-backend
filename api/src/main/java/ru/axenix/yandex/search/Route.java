package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route extends Detail {
    @JsonProperty("thread")
    private Thread thread;
    @JsonProperty("from")
    private Location from;
    @JsonProperty("to")
    private Location to;
    @JsonProperty("departure")
    private OffsetDateTime departure;
    @JsonProperty("arrival")
    private OffsetDateTime arrival;
}
