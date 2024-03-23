package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class InvalidRoleException extends RuntimeException {
    private String code;

    public InvalidRoleException(String code, String message) {
        super(message);
        this.code = code;
    }
}