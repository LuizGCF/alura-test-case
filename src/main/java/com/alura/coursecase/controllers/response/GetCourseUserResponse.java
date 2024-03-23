package com.alura.coursecase.controllers.response;

import com.alura.coursecase.models.UserModel;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetCourseUserResponse {
    private String name;
    private String username;
    private String email;

    public static GetCourseUserResponse fromModel(UserModel userModel) {
        return GetCourseUserResponse.builder()
                .name(userModel.getName())
                .email(userModel.getEmail())
                .username(userModel.getUsername())
                .build();
    }
}

