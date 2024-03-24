package com.alura.coursecase.utils;

import com.alura.coursecase.models.RegistrationModel;

import java.time.LocalDateTime;

public class RegistrationCreator {

    public static RegistrationModel createRegistrationToBeSaved(){
        return RegistrationModel.builder()
                .user(UserCreator.createValidUser())
                .course(CourseCreator.createValidCourse())
                .registrationDate(LocalDateTime.now())
                .build();
    }

    public static RegistrationModel createInvalidRegistration(){
        return RegistrationModel.builder()
                .user(UserCreator.createValidUser())
                .course(CourseCreator.createInvalidCourse())
                .registrationDate(LocalDateTime.now())
                .build();
    }

}
