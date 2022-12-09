package com.example.multichar.repositories;

import com.example.multichar.Entity.Tokens;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Tokens, Long> {
    public boolean existsByAccessToken(String accessToken);
    public boolean existsByRefreshToken(String refreshToken);
}
