package com.alura.coursecase.controllers.request;

import com.alura.coursecase.enums.RolesEnum;
import com.alura.coursecase.models.UserModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostUserRequest {

    @NotNull
    @NotEmpty(message = "The attribute name cannot be empty.")
    private String name;
    @NotNull
    @NotEmpty(message = "The attribute username cannot be empty.")
    @Pattern(regexp = "^[a-z]{1,20}+$", message = "The username must contain only lower case letters.")
    private String username;
    @NotNull
    @Email(message = "The attribute email must be valid.")
    @NotEmpty(message = "The attribute email cannot be empty.")
    private String email;
    @NotNull
    @NotEmpty(message = "The attribute password cannot be empty.")
    private String password;
    private RolesEnum role;

    public UserModel toUserModel() {
        return UserModel.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .role(role.name())
                .creationDate(LocalDateTime.now())
                .build();
    }
}
