package ru.axenix.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.axenix.dto.UserInfo;
import ru.axenix.service.UserService;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfo> info(@NotNull Principal principal) {
        log.info("UserController::info");

        return ResponseEntity.ok(userService.info(principal.getName()));
    }

}
