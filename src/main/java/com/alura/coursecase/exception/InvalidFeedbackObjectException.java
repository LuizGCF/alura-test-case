package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class InvalidFeedbackObjectException extends RuntimeException {
    private String code;

    public InvalidFeedbackObjectException(String code, String message) {
        super(message);
        this.code = code;
    }
}