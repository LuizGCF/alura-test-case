package com.alura.coursecase.enums;

public enum ErrorsEnum {
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
