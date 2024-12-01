package com.MovieFlix.MovieApi.exception;

public class EmptyFileException extends RuntimeException{

    public EmptyFileException(String message){
        super(message);
    }

}
