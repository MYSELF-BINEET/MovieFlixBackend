package com.MovieFlix.MovieApi.exception;

public class MovieNotFoundException extends RuntimeException{

    public MovieNotFoundException(String message){
        super(message);
    }

}
