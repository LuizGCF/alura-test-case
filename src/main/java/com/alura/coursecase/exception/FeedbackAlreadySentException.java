package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class FeedbackAlreadySentException extends RuntimeException {
    private String code;

    public FeedbackAlreadySentException(String code, String message) {
        super(message);
        this.code = code;
    }
}