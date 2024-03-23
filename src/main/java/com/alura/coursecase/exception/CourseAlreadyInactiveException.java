package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class CourseAlreadyInactiveException extends RuntimeException {
    private String code;

    public CourseAlreadyInactiveException(String code, String message) {
        super(message);
        this.code = code;
    }
}