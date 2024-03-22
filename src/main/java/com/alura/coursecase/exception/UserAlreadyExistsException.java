package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private String code;

    public UserAlreadyExistsException(String code, String message) {
        super(message);
        this.code = code;
    }
}