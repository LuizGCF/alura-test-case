package com.alura.coursecase.utils;

import com.alura.coursecase.enums.RolesEnum;
import com.alura.coursecase.models.UserModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class UserCreator {

    public static UserModel createUserToBeSaved() {
        return UserModel.builder()
                .name("John")
                .username("john")
                .password("password")
                .email("john@gmail.com")
                .role(RolesEnum.STUDENT.name())
                .build();
    }

    public static UserModel createValidUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return UserModel.builder()
                .id(1)
                .name("John")
                .username("john")
                .password(passwordEncoder.encode("password"))
                .email("john@gmail.com")
                .role(RolesEnum.STUDENT.name())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static UserModel createValidInstructorUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return UserModel.builder()
                .id(2)
                .name("Sam")
                .username("sam")
                .password(passwordEncoder.encode("password"))
                .email("sam@gmail.com")
                .role(RolesEnum.INSTRUCTOR.name())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static UserModel createValidInstructorUserToBeSaved() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return UserModel.builder()
                .name("Sam")
                .username("sam")
                .password(passwordEncoder.encode("password"))
                .email("sam@gmail.com")
                .role(RolesEnum.INSTRUCTOR.name())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static UserModel createValidUserWithPasswordDecoded() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return UserModel.builder()
                .id(1)
                .name("John")
                .username("john")
                .password("password")
                .email("john@gmail.com")
                .role(RolesEnum.STUDENT.name())
                .creationDate(LocalDateTime.now())
                .build();
    }
}
