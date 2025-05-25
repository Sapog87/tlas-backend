package ru.axenix.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.axenix.dto.Route;
import ru.axenix.dto.UserInfo;
import ru.axenix.service.UserRouteService;
import ru.axenix.service.UserService;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRouteService userRouteService;
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfo> info(@NotNull Principal principal) {
        log.info("UserController::info");

        return ResponseEntity.ok(userService.info(principal.getName()));
    }

    @PostMapping("/route")
    public ResponseEntity<Void> saveRoute(
            @RequestParam("id") String id,
            @NotNull Principal principal) {
        log.info("UserController::saveRoute with id {}", id);

        userRouteService.addRouteToUser(id, principal.getName());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/route")
    public ResponseEntity<List<Route>> getRoutes(
            @RequestParam(value = "page", defaultValue = "1") @Min(0) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) @Max(1000) Integer pageSize,
            @NotNull Principal principal) {
        log.info("UserController::getRoutes with page {} and pageSize {}", page, pageSize);

        return ResponseEntity.ok(
                userRouteService.findRoutesOfUser(page, pageSize, principal.getName())
        );
    }

    @DeleteMapping("/route")
    public ResponseEntity<Void> deleteRoute(
            @RequestParam("id") String id,
            @NotNull Principal principal) {
        log.info("UserController::deleteRoute with id {}", id);

        userRouteService.deleteRouteOfUser(id, principal.getName());

        return ResponseEntity.ok().build();
    }
}
