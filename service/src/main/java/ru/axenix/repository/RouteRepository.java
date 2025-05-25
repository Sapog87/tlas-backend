package ru.axenix.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.axenix.entity.Route;
import ru.axenix.entity.User;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, String> {
    List<Route> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    void deleteRouteByIdAndUser(String id, User user);

    @Query("SELECT r.id FROM Route r WHERE r.user = :user AND r.id IN :ids")
    List<String> findExistingIdsByUserIdAndIds(@Param("user") User user, @Param("ids") List<String> ids);
}
