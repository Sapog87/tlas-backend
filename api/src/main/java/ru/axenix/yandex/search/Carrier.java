package ru.axenix.yandex.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Carrier {
//    @JsonProperty("code")
//    private int code;
    @JsonProperty("title")
    private String title;
//    @JsonProperty("address")
//    private String address;
//    @JsonProperty("url")
//    private String url;
//    @JsonProperty("email")
//    private String email;
//    @JsonProperty("contacts")
//    private String contacts;
//    @JsonProperty("phone")
//    private String phone;
//    @JsonProperty("logo")
//    private String logo;
}
