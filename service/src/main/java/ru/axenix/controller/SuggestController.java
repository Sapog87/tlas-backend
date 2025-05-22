package ru.axenix.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.axenix.dto.Node;
import ru.axenix.service.NodeService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SuggestController {

    private final NodeService nodeService;

    @GetMapping("/suggest")
    public ResponseEntity<List<Node>> suggest(@RequestParam String part) {
        log.debug("SuggestController::suggest with part {}", part);

        return ok(nodeService.suggest(part));
    }

    @GetMapping("/node")
    public ResponseEntity<Node> find(@RequestParam String code) {
        log.info("SuggestController::find with code {}", code);

        return ok(nodeService.find(code));
    }
}
