package com.solution.testshop.exception;

public class CustomResponseException extends RuntimeException {

    public CustomResponseException(Long id) {
        super("Сущность не найдена, id="+id);
    }
}
