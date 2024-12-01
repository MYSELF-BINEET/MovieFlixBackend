package com.MovieFlix.MovieApi.repositories;


import com.MovieFlix.MovieApi.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository  extends JpaRepository<Movie,Integer> {
}

