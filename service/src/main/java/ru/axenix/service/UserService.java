package ru.axenix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.axenix.dto.UserInfo;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserInfo info(String username) {
        Objects.requireNonNull(username);

        //todo
        return new UserInfo().setName(username);
    }
}
