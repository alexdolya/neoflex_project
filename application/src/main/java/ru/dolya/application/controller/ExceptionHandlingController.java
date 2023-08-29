package ru.dolya.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dolya.application.exception.FeignClientCustomException;
import ru.dolya.application.exception.PreScoringException;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(PreScoringException.class)
    public ResponseEntity<String> handlePrescoringException(PreScoringException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignClientCustomException.class)
    public ResponseEntity<String> handleFeignClientException(FeignClientCustomException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.GATEWAY_TIMEOUT);
    }
}
