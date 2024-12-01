package com.MovieFlix.MovieApi.auth.service;


import com.MovieFlix.MovieApi.auth.entites.RefreshToken;
import com.MovieFlix.MovieApi.auth.entites.User;
import com.MovieFlix.MovieApi.auth.repositories.RefreshTokenRepository;
import com.MovieFlix.MovieApi.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository,RefreshTokenRepository refreshTokenRepository){
        this.userRepository=userRepository;
        this.refreshTokenRepository=refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String username){
        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(("User not found with email : " + username)));

        RefreshToken refreshToken = user.getRefreshToken();

        if(refreshToken==null){
            long refreshTokenValidity=5*60*60*10000;
            refreshToken= refreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();

            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }
}
