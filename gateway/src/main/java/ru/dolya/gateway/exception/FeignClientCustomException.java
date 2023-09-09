package ru.dolya.gateway.exception;

public class FeignClientCustomException extends RuntimeException{
    public FeignClientCustomException(Throwable throwable) {
        super(throwable);
    }
}
