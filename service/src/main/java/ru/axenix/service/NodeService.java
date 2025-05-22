package ru.axenix.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.axenix.dto.Node;
import ru.axenix.exception.YandexCodeNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NodeService {
    private final YandexCodeService yandexCodeService;

    public List<Node> suggest(String part) {
        if (!StringUtils.hasText(part)) {
            return yandexCodeService.getDefaultNodes();
        }
        return yandexCodeService.getNodesByPart(part);
    }

    public Node find(String yandexCode) {
        return Optional.ofNullable(yandexCodeService.getNode(yandexCode))
                .orElseThrow(() -> new YandexCodeNotFoundException("No such node: " + yandexCode));
    }
}
