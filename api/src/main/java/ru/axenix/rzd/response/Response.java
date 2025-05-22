package ru.axenix.rzd.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.axenix.rzd.response.error.Error;
import ru.axenix.rzd.response.result.Result;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({
        @JsonSubTypes.Type(Result.class),
        @JsonSubTypes.Type(Error.class)
})
public abstract class Response {
}
