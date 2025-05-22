package ru.axenix.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigurationProperties {
    @NotBlank
    private String secret;
    @Positive
    private long expirationAccessInMilliSeconds;
    @Positive
    private long expirationRefreshInMilliSeconds;
}
