package ru.axenix.service;

import jakarta.annotation.PostConstruct;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.axenix.dto.Node;
import ru.axenix.util.DataUtil;
import ru.axenix.yandex.station.Codes;
import ru.axenix.yandex.station.Region;
import ru.axenix.yandex.station.Settlement;
import ru.axenix.yandex.station.Station;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class YandexCodeService {
    private final DataUtil dataUtil;
    private Map<String, Codes> yandexCodesToCodes;
    private final Map<String, Node> yandexCodeToNode = new ConcurrentHashMap<>();
    private List<Pair<String, Node>> nodes;
    private final List<Node> defaultNodes = new ArrayList<>();
    private final Map<Character, Character> engToRus = new ConcurrentHashMap<>();

    {
        String eng = "qwertyuiop[]asdfghjkl;'zxcvbnm,.`";
        String rus = "йцукенгшщзхъфывапролджэячсмитьбюё";

        for (int i = 0; i < eng.length(); i++) {
            engToRus.put(eng.charAt(i), rus.charAt(i));
            engToRus.put(Character.toUpperCase(eng.charAt(i)), Character.toUpperCase(rus.charAt(i)));
        }
    }

    public Codes getCodes(String fromYandexCode) {
        return yandexCodesToCodes.get(fromYandexCode);
    }

    public Node getNode(String fromYandexCode) {
        return yandexCodeToNode.get(fromYandexCode);
    }

    public List<Node> getDefaultNodes() {
        return Collections.unmodifiableList(defaultNodes);
    }

    public List<Node> getNodesByPart(String part) {
        var search = toSearchState(part).replace(" ", "");
        var corrected = convertLayout(search);
        return nodes.stream()
                .filter(pair -> pair.getFirst().startsWith(corrected))
                .map(Pair::getSecond)
                .distinct()
                .limit(10)
                .toList();
    }

    private boolean isEnglishLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
               engToRus.containsKey(c); // на всякий случай
    }

    private String convertLayout(String input) {
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (isEnglishLetter(c)) {
                result.append(engToRus.getOrDefault(c, c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    @PostConstruct
    protected void init() {
        var regions = dataUtil.getRegions();
        yandexCodesToCodes = regions.stream()
                .flatMap(region -> region.settlements().stream())
                .flatMap(settlement -> Stream.concat(
                        Stream.of(settlement.codes()),
                        settlement.stations().stream().map(Station::codes)))
                .filter(x -> Objects.nonNull(x.yandexCode()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toConcurrentMap(Codes::yandexCode, Function.identity()),
                        Collections::unmodifiableMap)
                );

        var pairs = new ArrayList<Pair<String, Node>>();
        regions.forEach(region -> {
            region.settlements().forEach(settlement ->
                    processSettlement(settlement, region, pairs));
            region.settlements().forEach(settlement ->
                    settlement.stations().forEach(station ->
                            processStation(station, settlement, region, pairs)));
        });

        pairs.sort(Comparator
                .comparingInt((Pair<String, Node> nodePair) -> nodePair.getSecond().getId().length())
                .thenComparing((Pair<String, Node> nodePair) -> nodePair.getSecond().getId())
        );

        nodes = Collections.unmodifiableList(pairs);
        var s = Set.of("c2", "c39", "c213", "c43");
        defaultNodes.addAll(nodes.stream()
                .map(Pair::getSecond)
                .filter(node -> s.contains(node.getId()))
                .distinct()
                .toList()
        );
    }

    private void processSettlement(Settlement settlement, Region region, List<Pair<String, Node>> pairs) {
        if (!settlement.title().isBlank()) {
            var node = buildNode(
                    settlement.codes().yandexCode(),
                    "city",
                    settlement.title(),
                    region.title()
            );
            addSearchPartsToPairs(settlement.title(), node, pairs);
            yandexCodeToNode.put(settlement.codes().yandexCode(), node);
        }
    }

    private void processStation(Station station, Settlement settlement, Region region, List<Pair<String, Node>> pairs) {
        var node = buildNode(
                station.codes().yandexCode(),
                station.stationType(),
                station.title(),
                settlement.title().isBlank() ? region.title() : settlement.title()
        );
        addSearchPartsToPairs(station.title(), node, pairs);
        yandexCodeToNode.put(station.codes().yandexCode(), node);
    }

    private void addSearchPartsToPairs(String title, Node node, List<Pair<String, Node>> pairs) {
        var name = toSearchState(title).split(" ");
        for (int i = 0; i < name.length; i++) {
            pairs.add(new Pair<>(String.join("", Arrays.stream(name).skip(i).toList()), node));
        }
    }

    public String toSearchState(String title) {
        return title
                .replace("-", " ")
                .replaceAll("[.()]", "")
                .toLowerCase();
    }

    private Node buildNode(
            String yandexCode, String type,
            String title, String parentTitle) {
        var label = new StringBuilder();
        if (type != null) label.append(toReadableType(type));
        if (title != null) label.append(title).append(", ");
        if (parentTitle != null) label.append(parentTitle);
        return Node.builder()
                .id(yandexCode)
                .label(label.toString())
                .title(title)
                .build();
    }

    private String toReadableType(String type) {
        return switch (type) {
            case "station" -> "ст. ";
            case "airport" -> "a/п ";
            case "train_station" -> "вкз. ";
            case "platform" -> "пл. ";
            default -> "";
        };
    }
}
