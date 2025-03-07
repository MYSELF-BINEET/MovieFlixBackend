package com.MovieFlix.MovieApi.service;

import com.MovieFlix.MovieApi.dto.MovieDto;
import com.MovieFlix.MovieApi.dto.MoviePageResponse;
import com.MovieFlix.MovieApi.entities.Movie;
import com.MovieFlix.MovieApi.exception.FileExistsException;
import com.MovieFlix.MovieApi.exception.MovieNotFoundException;
import com.MovieFlix.MovieApi.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException, FileExistsException {

        if(Files.exists(Paths.get(path + File.separator +file.getOriginalFilename()))){
            throw new FileExistsException("File already exists ! Please enter another file name!");
        };
        // 1. upload file
        String uploadedFileName = fileService.uploadFile(path, file);

        // 2. set the value of field 'poster' as filename
        movieDto.setPoster(uploadedFileName);

        // 3. map dto to Movie object
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // 4. save the movie object -> saved Movie object
        Movie savedMovie = movieRepository.save(movie);

        // 5. generate the posterUrl
        String posterUrl =  baseUrl +  "/file/" + uploadedFileName;

        // 6. map Movie object to DTO object and return it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) throws MovieNotFoundException {
        // 1. check the data inn DB and if exits ,fetch  the data of given data
        Movie movie= movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found"));
        // 2. generate posterurl
        String posterUrl=baseUrl +"/file/" +movie.getPoster();

        // 3. map to movie dto object and return it
        MovieDto response=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return response;

    }

    @Override
    public List<MovieDto> getAllMovies() {
        // 1. fetch all the data from DB
        List<Movie> movies=movieRepository.findAll();

        List<MovieDto> moviesDto=new ArrayList<>();

        // 2. iterate through the list , generate posterurl for each movie obj,
        // and map to MovieDto obj

        for (Movie movie :movies){
            String posterUrl=baseUrl +"/file/" + movie.getPoster();
            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            moviesDto.add(response);
        }

        return moviesDto;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException, MovieNotFoundException {
        // 1. check if movie object exists with given movieId
        Movie mv = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie does not exits"));

        // 2. if file is null, do nothing
        // if file is not null, then delete existing file associated with the record,
        // and upload the new file
        String fileName = mv.getPoster();
        if (file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }

        // 3. set movieDto's poster value, according to step2
        movieDto.setPoster(fileName);

        // 4. map it to Movie object
        Movie movie = new Movie(
                mv.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // 5. save the movie object -> return saved movie object
        Movie updatedMovie = movieRepository.save(movie);

        // 6. generate posterUrl for it
        String posterUrl = baseUrl + "/file/" + fileName;

        // 7. map to MovieDto and return it
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException, MovieNotFoundException {
        // 1. check if movie object exists in DB
        Movie mv=movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie does not exits"));

        // 2. delete the file associated with this object
        Files.deleteIfExists(Paths.get(path+File.separator+mv.getPoster()));

        // 3. delete the movie object
        movieRepository.delete(mv);

        return "Movie deleted with id = "+ movieId;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable=  PageRequest.of(pageNumber,pageSize);

        Page<Movie> moviesPages=movieRepository.findAll(pageable);
        List<Movie> movies= moviesPages.getContent();

        List<MovieDto> movieDtos=new ArrayList<>();

        for(Movie movie:movies){
            String posterUrl=baseUrl +"/file/" +movie.getPoster();
            MovieDto movieDto=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return new MoviePageResponse(movieDtos,pageNumber,pageSize,
                (int) moviesPages.getTotalElements(),
                moviesPages.getTotalPages(),
                moviesPages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort=dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();



        Pageable pageable= (Pageable) PageRequest.of(pageNumber,pageSize,sort);

        Page<Movie> moviesPages=movieRepository.findAll((org.springframework.data.domain.Pageable) pageable);
        List<Movie> movies= moviesPages.getContent();

        List<MovieDto> movieDtos=new ArrayList<>();

        for(Movie movie:movies){
            String posterUrl=baseUrl +"/file/" +movie.getPoster();
            MovieDto movieDto=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(movieDto);
        }
        return new MoviePageResponse(movieDtos,pageNumber,pageSize,
                (int) moviesPages.getTotalElements(),
                moviesPages.getTotalPages(),
                moviesPages.isLast());
    }

}
