package com.alura.coursecase.enums;

public enum ErrorsEnum {
    A000("A-000", "Invalid token."),
    A001("A-001", "Error generating token."),
    A101("A-101", "User not found."),
    A102("A-102", "The %s is already in use."),
    A103("A-103", "Error validating parameters.");

    public String code;
    public String message;

    ErrorsEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
