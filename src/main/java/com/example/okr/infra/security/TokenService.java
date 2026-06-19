package com.example.okr.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.okr.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private static final String ISSUER = "OKRAPI";

    @Value("${api.security.secret}")
    private String apiSecret;

    @Value("${api.security.access-token-expiration-minutes:15}")
    private long accessTokenExpirationMinutes;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getUser_id())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error al generar el token", e);
        }
    }

    public String getSubject(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT == null ? null : decodedJWT.getSubject();
    }

    public Long getUserId(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT == null ? null : decodedJWT.getClaim("id").asLong();
    }

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank() || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7).trim();
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusMinutes(accessTokenExpirationMinutes).toInstant(ZoneOffset.of("-05:00"));
    }

    private DecodedJWT validateToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
