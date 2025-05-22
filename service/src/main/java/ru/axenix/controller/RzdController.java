package ru.axenix.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.axenix.dto.Tickets;
import ru.axenix.service.RzdCarService;

import java.time.OffsetDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RzdController {

    private final RzdCarService service;

    @GetMapping("/tickets")
    public ResponseEntity<Tickets> tickets(
            @RequestParam @NotBlank String origin,
            @RequestParam @NotBlank String destination,
            @RequestParam @NotNull OffsetDateTime date,
            @RequestParam @NotBlank String train
    ) {
        log.info("Start RzdController::tickets with origin: {}, destination: {}, date: {}, train: {}",
                origin, destination, date, train);

        var tickets = service.getTickets(origin, destination, date, train);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tickets);
    }
}
