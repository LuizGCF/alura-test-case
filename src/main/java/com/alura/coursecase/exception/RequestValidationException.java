package com.alura.coursecase.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class RequestValidationException extends RuntimeException {
    private BindingResult errors;

    public RequestValidationException(BindingResult errors) {
        super();
        this.errors = errors;
    }
}