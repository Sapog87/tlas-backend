package ru.axenix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.axenix.dto.SearchHistoryDto;
import ru.axenix.entity.SearchHistory;
import ru.axenix.exception.SearchHistoryNotFoundException;
import ru.axenix.exception.UserNotFoundException;
import ru.axenix.repository.SearchHistoryRepository;
import ru.axenix.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {
    private static final Limit DEFAUTL_LIMIT = Limit.of(10);
    private static final Sort DEFAULT_SORT = Sort.by(
            Sort.Order.desc("isFavorite"),
            Sort.Order.desc("count")
    );

    private final SearchHistoryRepository searchHistoryRepository;
    private final YandexCodeService yandexCodeService;
    private final UserRepository userRepository;

    @Transactional
    public List<SearchHistoryDto> getSearchHistory(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return searchHistoryRepository.findByUser(user, DEFAUTL_LIMIT, DEFAULT_SORT)
                .stream()
                .map(searchHistory ->
                        new SearchHistoryDto()
                                .setTo(searchHistory.getToYandexCode())
                                .setToTitle(yandexCodeService.getNode(searchHistory.getToYandexCode()).getTitle())
                                .setFrom(searchHistory.getFromYandexCode())
                                .setFromTitle(yandexCodeService.getNode(searchHistory.getFromYandexCode()).getTitle())
                                .setIsFavorite(searchHistory.getIsFavorite())
                ).toList();
    }

    @Transactional
    public void addSearchHistory(String username, String from, String to) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        searchHistoryRepository.findByUserAndFromYandexCodeAndToYandexCode(user, from, to)
                .ifPresentOrElse(
                        searchHistory -> {
                            searchHistory.setCount(searchHistory.getCount() + 1);
                            searchHistoryRepository.save(searchHistory);
                        }, () -> {
                            var searchHistory = new SearchHistory()
                                    .setUser(user)
                                    .setToYandexCode(to)
                                    .setFromYandexCode(from);
                            searchHistoryRepository.save(searchHistory);
                        }
                );
    }

    @Transactional
    public void setFavorite(String username, String from, String to, boolean favorite) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        searchHistoryRepository.findByUserAndFromYandexCodeAndToYandexCode(user, from, to)
                .ifPresentOrElse(searchHistory -> {
                            searchHistory.setIsFavorite(favorite);
                            searchHistoryRepository.save(searchHistory);
                        }, () -> {
                            throw new SearchHistoryNotFoundException(
                                    "username %s from %s to %s".formatted(username, from, to)
                            );
                        }
                );
    }

    @Transactional
    public void deleteHistory(String username, String from, String to) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        searchHistoryRepository.deleteByUserAndFromYandexCodeAndToYandexCode(user, from, to);
    }
}
