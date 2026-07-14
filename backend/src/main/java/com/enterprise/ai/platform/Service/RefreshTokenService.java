package com.enterprise.ai.platform.Service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.ai.platform.Model.RefreshToken;
import com.enterprise.ai.platform.Model.User;
import com.enterprise.ai.platform.Repository.RefreshTokenRepository;
import com.enterprise.ai.platform.Repository.UserRepository;

@Service
public class RefreshTokenService {

    @Value("${app.jwtRefreshExpirationMs:604800000}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @Transactional
    public RefreshToken createRefreshToken(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush(); // Force the DELETE to hit the DB before the INSERT!

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setRevoked(false);

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        if (token.isRevoked()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token is revoked. Please make a new signin request");
        }
        return token;
    }
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    public int deleteByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return refreshTokenRepository.deleteByUser(user);
    }
}
