package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException {
    private String code;

    public InvalidTokenException(String code, String message) {
        super(message);
        this.code = code;
    }
}