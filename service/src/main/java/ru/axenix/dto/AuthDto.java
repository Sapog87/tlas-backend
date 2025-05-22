package ru.axenix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
