package com.newtrendz.pass.repository;

import com.newtrendz.pass.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken,String> {
    Optional<RefreshToken> findByToken(String refreshToken);

    RefreshToken findByUserId(String id);

    @Transactional
    RefreshToken deleteByUserId(String userId);

}
