package ru.dolya.deal.exception;

public class FeignClientCustomException extends RuntimeException {
    public FeignClientCustomException(Throwable throwable) {
        super(throwable);
    }
}
