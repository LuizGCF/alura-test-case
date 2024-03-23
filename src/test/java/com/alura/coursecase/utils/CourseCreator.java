package com.alura.coursecase.utils;

import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.models.CourseModel;

import java.time.LocalDateTime;

public class CourseCreator {

    public static CourseModel createCourseToBeSaved(){
        return CourseModel.builder()
                .instructor(UserCreator.createValidInstructorUser())
                .name("Java Course")
                .code("spring")
                .description("Java course using spring boot")
                .status(StatusEnum.ACTIVE.name())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static CourseModel createValidCourse(){
        return CourseModel.builder()
                .id(1)
                .instructor(UserCreator.createValidInstructorUser())
                .name("Java Course")
                .code("spring")
                .description("Java course using spring boot")
                .status(StatusEnum.ACTIVE.name())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static CourseModel createInvalidCourse(){
        return CourseModel.builder()
                .id(1)
                .instructor(UserCreator.createValidInstructorUser())
                .name("Java Course")
                .code("spring")
                .description("Java course using spring boot")
                .status(StatusEnum.INACTIVE.name())
                .creationDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .build();
    }
}
