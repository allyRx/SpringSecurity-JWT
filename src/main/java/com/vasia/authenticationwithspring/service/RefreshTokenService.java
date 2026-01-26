package com.vasia.authenticationwithspring.service;


import com.vasia.authenticationwithspring.entity.RefreshToken;
import com.vasia.authenticationwithspring.entity.User;
import com.vasia.authenticationwithspring.repository.RefreshTokenRepository;
import com.vasia.authenticationwithspring.repository.UserRepository;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${security.jwt.refresh-expiration}")
    private Long refreshExpiration;
    private final RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository , UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository=userRepository;
    }

    @Transactional
    public RefreshToken create(Long userId){

        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));

        refreshTokenRepository.deleteByUser(user);

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser(user);
        newRefreshToken.setToken(UUID.randomUUID().toString());
        newRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));

        return refreshTokenRepository.save(newRefreshToken);
    }

    public RefreshToken verify(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token invalide"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expiré");
        }

        return refreshToken;
    }

    public void deleteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        refreshTokenRepository.deleteByUser(user);
    }
}
