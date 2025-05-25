package ru.axenix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.axenix.dto.UserInfo;
import ru.axenix.exception.UserNotFoundException;
import ru.axenix.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfo info(String username) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return new UserInfo()
                .setName(user.getName())
                .setUsername(user.getUsername())
                .setRegistrationDate(user.getCreatedAt());
    }
}
