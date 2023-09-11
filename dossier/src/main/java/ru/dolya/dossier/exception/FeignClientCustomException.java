package ru.dolya.dossier.exception;

public class FeignClientCustomException extends RuntimeException{
    public FeignClientCustomException(Throwable throwable) {
        super(throwable);
    }
}
