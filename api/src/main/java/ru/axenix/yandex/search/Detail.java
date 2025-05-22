package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "is_transfer",
        defaultImpl = Route.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Route.class),
        @JsonSubTypes.Type(value = Transfer.class, name = "true"),
})
public abstract class Detail {
}
