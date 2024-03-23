package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class CourseCodeAlreadyExistsException extends RuntimeException {
    private String code;

    public CourseCodeAlreadyExistsException(String code, String message) {
        super(message);
        this.code = code;
    }
}