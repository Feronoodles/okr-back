package com.example.okr.persistence;

import com.example.okr.entities.RefreshToken;
import com.example.okr.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenKey(String tokenKey);

    List<RefreshToken> findAllByUserAndRevokedAtIsNull(User user);
}
