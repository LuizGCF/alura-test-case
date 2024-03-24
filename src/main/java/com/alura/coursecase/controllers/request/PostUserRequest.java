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

    @NotEmpty(message = "The attribute name is required.")
    private String name;

    @NotEmpty(message = "The attribute username is required.")
    @Pattern(regexp = "^[a-z]{1,20}+$", message = "The username must contain only lower case letters up to 20 characters.")
    private String username;

    @Email(message = "The attribute email must be valid.")
    @NotEmpty(message = "The attribute email is required.")
    private String email;

    @NotEmpty(message = "The attribute password is required.")
    private String password;
    @NotNull(message = "The attribute role is required.")
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
