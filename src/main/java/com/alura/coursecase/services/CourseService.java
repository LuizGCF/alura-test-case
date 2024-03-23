package com.alura.coursecase.services;

import com.alura.coursecase.controllers.response.GetCourseResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.enums.RolesEnum;
import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.exception.*;
import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.repositories.CourseRepository;
import com.alura.coursecase.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    public void createCourse(CourseModel courseModel) {
        if (courseRepository.existsByCode(courseModel.getCode()))
            throw new CourseCodeAlreadyExistsException(ErrorsEnum.A201.code, ErrorsEnum.A201.message);

        Optional<String> userRole = userRepository.findRoleById(courseModel.getInstructor().getId());

        if(!userRole.isPresent())
            throw new UserNotFoundException(ErrorsEnum.A101.code, ErrorsEnum.A101.message);

        if(!userRole.get().equals(RolesEnum.INSTRUCTOR.name()))
            throw new InvalidRoleException(ErrorsEnum.A202.code, ErrorsEnum.A202.message);

        courseRepository.save(courseModel);
    }

    public void deactivateCourse(String code) {
        Optional<CourseModel> optionalCourse = courseRepository.findByCode(code);
        if(!optionalCourse.isPresent())
            throw new CourseNotFoundException(ErrorsEnum.A203.code, ErrorsEnum.A203.message);

        CourseModel course = optionalCourse.get();

        if(course.getStatus().equals(StatusEnum.INACTIVE.name()))
            throw new CourseAlreadyInactiveException(ErrorsEnum.A204.code, ErrorsEnum.A204.message);


        course.setStatus(StatusEnum.INACTIVE.name());
        course.setInactivationDate(LocalDateTime.now());

        courseRepository.save(course);
    }

    public Page<GetCourseResponse> listCourses(Integer page, Integer size, StatusEnum status){
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseModel> courses;
        if(Objects.nonNull(status))
            courses = courseRepository.findByStatus(status.name(), pageable);
        else
            courses = courseRepository.findAll(pageable);

        return courses.map(obj -> GetCourseResponse.fromModel(obj));
    }
}
