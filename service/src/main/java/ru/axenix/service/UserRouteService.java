package ru.axenix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.axenix.dto.Segment;
import ru.axenix.dto.Transport;
import ru.axenix.entity.Route;
import ru.axenix.exception.RouteNotFoundException;
import ru.axenix.exception.UserNotFoundException;
import ru.axenix.repository.RouteRedisRepository;
import ru.axenix.repository.RouteRepository;
import ru.axenix.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRouteService {
    private final RouteRepository routeRepository;
    private final RouteRedisRepository routeRedisRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addRouteToUser(String key, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        var redisRoute = routeRedisRepository.findByKey(key);
        if (redisRoute == null)
            throw new RouteNotFoundException(key);

        if (routeRepository.existsById(key))
            return;

        var route = new Route()
                .setId(key)
                .setUser(user)
                .setValue(mapToRouteJson(redisRoute));

        routeRepository.save(route);
    }

    @Transactional(readOnly = true)
    public List<ru.axenix.dto.Route> findRoutesOfUser(Integer page, Integer pageSize, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var routes = routeRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(page, pageSize));

        return routes.stream().map(this::mapToRoute).toList();
    }

    @Transactional(readOnly = true)
    public List<String> findSavedRoutesFromIdsOfUser(List<String> ids, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return routeRepository.findExistingIdsByUserIdAndIds(user, ids);
    }

    private ru.axenix.dto.Route mapToRoute(Route route) {
        return new ru.axenix.dto.Route()
                .setKey(route.getId())
                .setTransfers(route.getValue().getTransfers())
                .setStartDateTime(route.getValue().getStartDateTime())
                .setFinishDateTime(route.getValue().getFinishDateTime())
                .setStartLocation(route.getValue().getStartLocation())
                .setFinishLocation(route.getValue().getFinishLocation())
                .setSegments(route.getValue().getSegments().stream()
                        .map(segment -> new Segment()
                                .setVehicle(segment.getVehicle())
                                .setRaceNumber(segment.getRaceNumber())
                                .setCarrier(segment.getCarrier())
                                .setStartDateTime(segment.getStartDateTime())
                                .setFinishDateTime(segment.getFinishDateTime())
                                .setStartStation(segment.getStartStation())
                                .setFinishStation(segment.getFinishStation())
                                .setOriginCode(segment.getOriginCode())
                                .setDestinationCode(segment.getDestinationCode())
                                .setFinishCity(segment.getFinishCity())
                                .setStartCity(segment.getStartCity())
                                .setTransport(switch (segment.getTransport()) {
                                    case PLANE -> Transport.PLANE;
                                    case TRAIN -> Transport.TRAIN;
                                    case SUBURBAN -> Transport.SUBURBAN;
                                    case BUS -> Transport.BUS;
                                })
                                .setProducts(List.of())
                        ).toList()
                );
    }

    private Route.RouteJson mapToRouteJson(ru.axenix.dto.Route route) {
        return new Route.RouteJson()
                .setStartDateTime(route.getStartDateTime())
                .setFinishDateTime(route.getFinishDateTime())
                .setTransfers(route.getTransfers())
                .setStartLocation(route.getStartLocation())
                .setFinishLocation(route.getFinishLocation())
                .setSegments(route.getSegments().stream()
                        .map(segment -> new Route.SegmentJson()
                                .setVehicle(segment.getVehicle())
                                .setRaceNumber(segment.getRaceNumber())
                                .setCarrier(segment.getCarrier())
                                .setStartDateTime(segment.getStartDateTime())
                                .setFinishDateTime(segment.getFinishDateTime())
                                .setStartStation(segment.getStartStation())
                                .setFinishStation(segment.getFinishStation())
                                .setOriginCode(segment.getOriginCode())
                                .setDestinationCode(segment.getDestinationCode())
                                .setFinishCity(segment.getFinishCity())
                                .setStartCity(segment.getStartCity())
                                .setTransport(switch (segment.getTransport()) {
                                    case PLANE -> Route.Transport.PLANE;
                                    case TRAIN -> Route.Transport.TRAIN;
                                    case SUBURBAN -> Route.Transport.SUBURBAN;
                                    case BUS -> Route.Transport.BUS;
                                })
                        ).toList()
                );
    }

    @Transactional
    public void deleteRouteOfUser(String id, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        routeRepository.deleteRouteByIdAndUser(id, user);
    }
}
