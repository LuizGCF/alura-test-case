package com.alura.coursecase.enums;

public enum ErrorsEnum {
    //System related errors.
    A000("A-000", "Invalid token."),
    A001("A-001", "Error generating token."),
    A002("A-002", "Error validating parameters."),
    //User entity related errors.
    A101("A-101", "User not found."),
    A102("A-102", "The %s is already in use."),
    //Course entity related errors.
    A201("A-201", "The code provided for the course is already in use."),
    A202("A-202", "Only instructors can author a course."),
    A203("A-203", "Course not found."),
    A204("A-204", "Course is already inactive."),
    //Registration entity related errors.
    A301("A-301", "User is already registered to this course."),
    A302("A-302", "Registration not found."),
    //Feedback entity related errors.
    A401("A-401","Feedback for this course was already sent."),
    A402("A-402","Feedback not found."),
    A403("A-403","A title and a description are required when rating is below 6.");

    public String code;
    public String message;

    ErrorsEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
