package com.alura.coursecase.services;

import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.exception.CourseAlreadyInactiveException;
import com.alura.coursecase.exception.CourseNotFoundException;
import com.alura.coursecase.exception.UserAlreadyRegisteredException;
import com.alura.coursecase.exception.UserNotFoundException;
import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.models.RegistrationModel;
import com.alura.coursecase.repositories.CourseRepository;
import com.alura.coursecase.repositories.RegistrationRepository;
import com.alura.coursecase.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    public void register(RegistrationModel registrationModel) {
        if(!userRepository.existsById(registrationModel.getUser().getId()))
            throw new UserNotFoundException(ErrorsEnum.A101.code, ErrorsEnum.A101.message);

        Optional<CourseModel> course = courseRepository.findById(registrationModel.getCourse().getId());

        if(course.isEmpty())
            throw new CourseNotFoundException(ErrorsEnum.A203.code, ErrorsEnum.A203.message);

        if(course.get().getStatus().equals(StatusEnum.INACTIVE.name()))
            throw new CourseAlreadyInactiveException(ErrorsEnum.A204.code, ErrorsEnum.A204.message);

        if(registrationRepository.existsByIdUserAndIdCourse(registrationModel.getUser().getId(), registrationModel.getCourse().getId()))
            throw new UserAlreadyRegisteredException(ErrorsEnum.A301.code, ErrorsEnum.A301.message);

        registrationRepository.save(registrationModel);

    }
}
