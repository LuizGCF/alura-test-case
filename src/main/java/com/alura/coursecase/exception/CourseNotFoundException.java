package com.alura.coursecase.exception;

import lombok.Getter;

@Getter
public class CourseNotFoundException extends RuntimeException {
    private String code;

    public CourseNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}