package com.alura.coursecase.controllers.request;

import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.models.UserModel;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostCourseRequest {

    @NotNull
    @NotEmpty(message = "The attribute name cannot be empty.")
    private String name;

    @NotNull
    @NotEmpty(message = "The attribute code cannot be empty.")
    @Pattern(regexp = "^[a-z\\-]{1,10}+$", message = "The code must contain only lower case letters and hyphens up to 10 characters.")
    private String code;

    @NotNull(message = "The attribute id_instructor cannot be null.")
    @Range(min = 1)
    @JsonAlias("id_instructor")
    private Integer idInstructor;

    @NotNull
    @NotEmpty(message = "The description username cannot be empty.")
    private String description;

    public CourseModel toCourseModel() {
        return CourseModel.builder()
                .name(name)
                .code(code)
                .instructor(new UserModel(idInstructor))
                .description(description)
                .status(StatusEnum.ACTIVE.name())
                .creationDate(LocalDateTime.now())
                .build();
    }
}
