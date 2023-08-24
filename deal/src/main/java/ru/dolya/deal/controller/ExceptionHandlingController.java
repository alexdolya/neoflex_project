package ru.dolya.deal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dolya.deal.exception.FeignClientCustomException;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(FeignClientCustomException.class)
    public ResponseEntity<String> handleFeignClientException(FeignClientCustomException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.GATEWAY_TIMEOUT);
    }
}
