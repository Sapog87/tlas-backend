package ru.axenix.controller;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.axenix.dto.SearchHistoryDto;
import ru.axenix.service.SearchHistoryService;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchHistoryController {
    private final SearchHistoryService service;

    @GetMapping("/history")
    public ResponseEntity<List<SearchHistoryDto>> getSearchHistory(Principal principal) {
        log.info("SearchHistoryController::getSearchHistory");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getSearchHistory(principal.getName()));
    }

    @PostMapping("/history/favorite")
    public ResponseEntity<List<SearchHistoryDto>> setFavorite(
            @RequestParam @NotBlank String from,
            @RequestParam @NotBlank String to,
            @RequestParam @NotNull Boolean favorite,
            Principal principal
    ) {
        log.info("SearchHistoryController::setFavorite with from {} to {} favorite {}",
                from, to, favorite);

        service.setFavorite(principal.getName(), from, to, favorite);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/history/delete")
    public ResponseEntity<List<SearchHistoryDto>> deleteHistory(
            @RequestParam @NotBlank String from,
            @RequestParam @NotBlank String to,
            Principal principal
    ) {
        log.info("SearchHistoryController::deleteHistory with from {} to {} ",
                from, to);

        service.deleteHistory(principal.getName(), from, to);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
