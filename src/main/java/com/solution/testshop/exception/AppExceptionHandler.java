package com.solution.testshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({CustomResponseException.class, NullOrderException.class})
    protected ResponseEntity<Object> handleEntityNotFoundEx(RuntimeException ex, WebRequest request) {
        AppError apiError = new AppError(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleEntityNotCorrectEx(ConstraintViolationException ex, WebRequest request) {
        AppError apiError = new AppError("Данные для сущности указаны неккоректно!");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
