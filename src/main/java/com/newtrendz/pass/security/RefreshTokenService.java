package com.newtrendz.pass.security;

import com.newtrendz.pass.entity.RefreshToken;
import com.newtrendz.pass.entity.User;
import com.newtrendz.pass.exceptions.MasterNotFoundException;
import com.newtrendz.pass.repository.RefreshTokenRepository;
import com.newtrendz.pass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();
        Optional<User> getUserId = userRepository.findById(userId);
        refreshToken.setUserId(userId);
        refreshToken.setUserDetail(getUserId.get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(86400000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new MasterNotFoundException("Refresh token was expired with this ID "+token.getToken()+" , Please make a new signin request");
        }
        return token;
    }

    public void deleteByUserId(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
