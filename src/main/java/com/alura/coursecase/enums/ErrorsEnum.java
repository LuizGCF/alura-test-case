package com.alura.coursecase.enums;

public enum ErrorsEnum {
    A000("A-000", "Invalid token."),
    A001("A-001", "Error generating token."),
    A002("A-002", "Error validating parameters."),
    A101("A-101", "User not found."),
    A102("A-102", "The %s is already in use."),
    A201("A-201", "The code provided for the course is already in use."),
    A202("A-202", "Only instructors can author a course."),
    A203("A-203", "Course not found."),
    A204("A-204", "Course is already inactive.");

    public String code;
    public String message;

    ErrorsEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
