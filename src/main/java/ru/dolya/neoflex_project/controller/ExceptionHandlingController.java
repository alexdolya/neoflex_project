package ru.dolya.neoflex_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dolya.neoflex_project.exception.PreScoringException;
import ru.dolya.neoflex_project.exception.ScoringException;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(PreScoringException.class)
    public ResponseEntity<String> handlePrescoringException(PreScoringException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ScoringException.class)
    public ResponseEntity<String> handleScoringException(ScoringException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
