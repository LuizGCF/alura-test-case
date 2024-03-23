package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class TokenGenerationFailedException extends RuntimeException {
    private String code;

    public TokenGenerationFailedException(String code, String message) {
        super(message);
        this.code = code;
    }
}