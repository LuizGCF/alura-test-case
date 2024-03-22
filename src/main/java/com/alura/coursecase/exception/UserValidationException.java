package com.alura.coursecase.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class UserValidationException extends RuntimeException {
    private BindingResult errors;

    public UserValidationException(BindingResult errors) {
        super();
        this.errors = errors;
    }
}