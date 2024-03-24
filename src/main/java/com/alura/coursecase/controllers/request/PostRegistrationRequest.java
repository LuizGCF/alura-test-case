package com.alura.coursecase.controllers.request;

import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.models.RegistrationModel;
import com.alura.coursecase.models.UserModel;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostRegistrationRequest {

    @NotNull(message= "The attribute id_user is required.")
    @JsonAlias("id_user")
    private Integer idUser;

    @NotNull(message= "The attribute id_course is required.")
    @JsonAlias("id_course")
    private Integer idCourse;

    public RegistrationModel toRegistrationModel(){
        return RegistrationModel.
                builder()
                .user(new UserModel(idUser))
                .course(new CourseModel(idCourse))
                .registrationDate(LocalDateTime.now())
                .build();
    }

}
