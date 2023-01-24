package com.solution.testshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Сущность не существует")
public class CustomResponseException extends RuntimeException {

}
