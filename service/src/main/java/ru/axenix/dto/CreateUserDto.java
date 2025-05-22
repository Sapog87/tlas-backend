package ru.axenix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserDto {
    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
