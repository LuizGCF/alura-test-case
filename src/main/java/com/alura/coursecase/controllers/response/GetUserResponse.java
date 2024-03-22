package com.alura.coursecase.controllers.response;

import com.alura.coursecase.models.UserModel;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetUserResponse {
    private String name;
    private String email;
    private String role;

    public static GetUserResponse fromModel(UserModel userModel) {
        return GetUserResponse.builder()
                .name(userModel.getName())
                .email(userModel.getEmail())
                .role(userModel.getRole())
                .build();
    }
}
