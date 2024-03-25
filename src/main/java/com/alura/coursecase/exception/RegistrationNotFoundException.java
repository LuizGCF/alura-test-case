package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class RegistrationNotFoundException extends RuntimeException {
    private String code;

    public RegistrationNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}