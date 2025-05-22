package ru.axenix.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.axenix.entity.SearchHistory;
import ru.axenix.entity.User;

import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    Optional<SearchHistory> findByUserAndFromYandexCodeAndToYandexCode(User user, String from, String to);

    List<SearchHistory> findByUser(User user, Limit limit, Sort sort);

    void deleteByUserAndFromYandexCodeAndToYandexCode(User user, String fromYandexCode, String toYandexCode);
}
