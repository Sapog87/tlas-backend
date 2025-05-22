package ru.axenix.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.axenix.dto.Route;
import ru.axenix.service.RouteService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @GetMapping("/rzd")
    public List<Route> getRzdRoutes(
            @RequestParam @NotBlank String fromYandexCode,
            @RequestParam @NotBlank String toYandexCode,
            @RequestParam @NotNull LocalDate date
    ) {
        log.info("RouteController::getRzdRoutes with fromYandexCode {}, toYandexCode {}, date {}",
                fromYandexCode, toYandexCode, date);

        return routeService.findOneWayRoutesRzd(fromYandexCode, toYandexCode, date);
    }

    @GetMapping("/yandex")
    public List<Route> getYandexRoutes(
            @RequestParam @NotBlank String fromYandexCode,
            @RequestParam @NotBlank String toYandexCode,
            @RequestParam @NotNull LocalDate date
    ) {
        log.info("RouteController::getYandexRoutes with fromYandexCode {}, toYandexCode {}, date {}",
                fromYandexCode, toYandexCode, date);

        return routeService.findOneWayRoutesYandex(fromYandexCode, toYandexCode, date);
    }
}
