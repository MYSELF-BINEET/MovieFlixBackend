package com.MovieFlix.MovieApi.auth.repositories;

import com.MovieFlix.MovieApi.auth.entites.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
}
