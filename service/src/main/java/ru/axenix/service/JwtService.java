package ru.axenix.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.axenix.security.JwtConfigurationProperties;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtService {
    private final JwtConfigurationProperties properties;
    private final SecretKey key;

    public JwtService(JwtConfigurationProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, properties.getExpirationAccessInMilliSeconds());
    }

    private String generateToken(UserDetails userDetails, long expirationMillis) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        var username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
    }
}
