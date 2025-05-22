package ru.axenix.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.axenix.entity.User;
import ru.axenix.exception.AuthException;
import ru.axenix.exception.RoleNotFoundException;
import ru.axenix.exception.UserAlreadyExists;
import ru.axenix.repository.RoleRepository;
import ru.axenix.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new AuthException("Incorrect username or password", e);
        }

        var userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.generateAccessToken(userDetails);
    }

    @Transactional
    public String signup(String name, String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExists(username);
        }

        var userRole = roleRepository
                .findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("USER"));

        var user = new User()
                .setName(name)
                .setUsername(username)
                .setPasswordHash(passwordEncoder.encode(password))
                .setRoles(Set.of(userRole));

        userRepository.save(user);
        var userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.generateAccessToken(userDetails);
    }
}
