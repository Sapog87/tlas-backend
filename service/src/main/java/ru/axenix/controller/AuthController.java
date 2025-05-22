package ru.axenix.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.axenix.dto.AuthDto;
import ru.axenix.dto.CreateUserDto;
import ru.axenix.dto.JwtResponse;
import ru.axenix.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signup(@RequestBody @Valid CreateUserDto createUserDto) {
        log.info("AuthController::signup");

        var jwt = authService.signup(
                createUserDto.getName(),
                createUserDto.getUsername(),
                createUserDto.getPassword()
        );

        return ResponseEntity.ok(new JwtResponse().setToken(jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid AuthDto authDto) {
        log.info("AuthController::login");

        var jwt = authService.login(authDto.getUsername(), authDto.getPassword());

        return ResponseEntity.ok(new JwtResponse().setToken(jwt));
    }
}
