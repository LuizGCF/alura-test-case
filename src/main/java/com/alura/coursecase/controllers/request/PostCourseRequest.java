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

    @NotEmpty(message = "The attribute name is required.")
    private String name;

    @NotEmpty(message = "The attribute code is required.")
    @Pattern(regexp = "^[a-z\\-]{1,10}+$", message = "The code must contain only lower case letters and hyphens up to 10 characters.")
    private String code;

    @NotNull(message = "The attribute id_instructor is required.")
    @Range(min = 1)
    @JsonAlias("id_instructor")
    private Integer idInstructor;

    @NotEmpty(message = "The description username is required.")
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
