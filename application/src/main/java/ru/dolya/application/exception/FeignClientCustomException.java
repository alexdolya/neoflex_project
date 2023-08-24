package ru.dolya.application.exception;

public class FeignClientCustomException extends RuntimeException{
    public FeignClientCustomException(Throwable throwable) {
        super(throwable);
    }
}
