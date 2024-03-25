package com.alura.coursecase.controllers.request;

import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.models.RegistrationModel;
import com.alura.coursecase.models.UserModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostRegistrationRequest {

    @NotNull(message= "The attribute id_user is required.")
    @Range(min = 1)
    private Integer idUser;

    @NotNull(message= "The attribute id_course is required.")
    @Range(min = 1)
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
