package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "has_transfers"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleRouteSegment.class, name = "false"),
        @JsonSubTypes.Type(value = MultiRouteSegment.class, name = "true"),
})
public abstract class Segment {
    @JsonProperty("departure")
    private OffsetDateTime departure;
    @JsonProperty("arrival")
    private OffsetDateTime arrival;
}
