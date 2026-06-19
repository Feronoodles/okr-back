package com.example.okr.service;

import com.example.okr.entities.RefreshToken;
import com.example.okr.entities.User;
import com.example.okr.infra.security.DataJWT;
import com.example.okr.infra.security.TokenService;
import com.example.okr.persistence.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${api.security.refresh-token-expiration-days:30}")
    private long refreshTokenExpirationDays;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, BCryptPasswordEncoder passwordEncoder, TokenService tokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public DataJWT createSession(User user) {
        String accessToken = tokenService.generateToken(user);
        String refreshToken = createRefreshToken(user);
        return new DataJWT(accessToken, refreshToken);
    }

    @Transactional
    public DataJWT refreshSession(String rawRefreshToken) {
        RefreshToken refreshToken = validateRefreshToken(rawRefreshToken);

        if (refreshToken.isRevoked() || refreshToken.isExpired()) {
            refreshToken.revoke();
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token invalido o expirado");
        }

        refreshToken.markAsUsed();
        refreshToken.revoke();

        User user = refreshToken.getUser();
        String accessToken = tokenService.generateToken(user);
        String nextRefreshToken = createRefreshToken(user);
        return new DataJWT(accessToken, nextRefreshToken);
    }

    @Transactional
    public void revokeToken(String rawRefreshToken, User user) {
        RefreshToken refreshToken = validateRefreshToken(rawRefreshToken);
        if (!refreshToken.getUser().getUser_id().equals(user.getUser_id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La sesion no pertenece al usuario autenticado");
        }
        refreshToken.revoke();
    }

    @Transactional
    public void revokeAll(User user) {
        refreshTokenRepository.findAllByUserAndRevokedAtIsNull(user)
                .forEach(RefreshToken::revoke);
    }

    private String createRefreshToken(User user) {
        String tokenKey = UUID.randomUUID().toString();
        String tokenSecret = generateTokenSecret();

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenKey(tokenKey)
                .tokenHash(passwordEncoder.encode(tokenSecret))
                .expiresAt(Date.from(LocalDateTime.now().plusDays(refreshTokenExpirationDays).toInstant(ZoneOffset.of("-05:00"))))
                .user(user)
                .build();

        refreshTokenRepository.save(refreshToken);
        return tokenKey + "." + tokenSecret;
    }

    private RefreshToken validateRefreshToken(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token requerido");
        }

        String[] parts = rawRefreshToken.split("\\.", 2);
        if (parts.length != 2) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Formato de refresh token invalido");
        }

        RefreshToken refreshToken = refreshTokenRepository.findByTokenKey(parts[0])
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token invalido"));

        if (!passwordEncoder.matches(parts[1], refreshToken.getTokenHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token invalido");
        }

        return refreshToken;
    }

    private String generateTokenSecret() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
