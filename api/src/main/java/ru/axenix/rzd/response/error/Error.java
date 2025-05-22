package ru.axenix.rzd.response.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.axenix.rzd.response.Response;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("error")
public class Error extends Response {
    @JsonProperty("code")
    private int code;
    @JsonProperty("message")
    private String message;
}
