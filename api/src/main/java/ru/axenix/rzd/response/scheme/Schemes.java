package ru.axenix.rzd.response.scheme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schemes {
    @JsonProperty("total_count")
    public int totalCount;
    @JsonProperty("scroll_id")
    public String scrollId;
    @JsonProperty("car_schemas")
    public List<CarSchema> carSchemas;
}
