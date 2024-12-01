package com.MovieFlix.MovieApi.service;


import com.MovieFlix.MovieApi.dto.MovieDto;
import com.MovieFlix.MovieApi.dto.MoviePageResponse;
import com.MovieFlix.MovieApi.entities.Movie;
import com.MovieFlix.MovieApi.exception.FileExistsException;
import com.MovieFlix.MovieApi.exception.MovieNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException, FileExistsException;

    MovieDto getMovie(Integer movieId) throws MovieNotFoundException;

    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Integer movieId,MovieDto movieDto,
                         MultipartFile file) throws IOException, MovieNotFoundException;

    String deleteMovie(Integer movieId) throws IOException, MovieNotFoundException;

    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);

    MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber,Integer pageSize,
                                                           String sortBy,String dir);

}
