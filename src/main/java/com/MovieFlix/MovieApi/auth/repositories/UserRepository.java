package com.MovieFlix.MovieApi.auth.repositories;

import com.MovieFlix.MovieApi.auth.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
}
