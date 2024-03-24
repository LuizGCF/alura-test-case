package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class UserAlreadyRegisteredException extends RuntimeException {
    private String code;

    public UserAlreadyRegisteredException(String code, String message) {
        super(message);
        this.code = code;
    }
}