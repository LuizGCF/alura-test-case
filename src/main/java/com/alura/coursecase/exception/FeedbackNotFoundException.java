package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class FeedbackNotFoundException extends RuntimeException {
    private String code;

    public FeedbackNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}