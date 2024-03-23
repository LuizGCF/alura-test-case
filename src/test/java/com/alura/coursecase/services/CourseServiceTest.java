package com.alura.coursecase.services;

import com.alura.coursecase.controllers.response.GetCourseResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.enums.RolesEnum;
import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.exception.*;
import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.repositories.CourseRepository;
import com.alura.coursecase.repositories.UserRepository;
import com.alura.coursecase.utils.CourseCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseServiceTest {

    @InjectMocks
    CourseService courseService;

    @Mock
    CourseRepository courseRepository;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("Creates a course when payload is valid")
    public void createCourse_ReturnsSuccessful_WhenValid() {
        CourseModel course = CourseCreator.createCourseToBeSaved();
        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(userRepository.findRoleById(anyInt())).thenReturn(Optional.of(RolesEnum.INSTRUCTOR.name()));

        courseService.createCourse(course);

        verify(courseRepository, times(1)).existsByCode(anyString());
        verify(userRepository, times(1)).findRoleById(anyInt());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("Returns error when course code already exists")
    public void createCourse_ReturnsError_WhenCourseCodeExists() throws CourseCodeAlreadyExistsException {
        CourseModel course = CourseCreator.createCourseToBeSaved();

        when(courseRepository.existsByCode(anyString())).thenReturn(true);

        CourseCodeAlreadyExistsException exception = assertThrows(CourseCodeAlreadyExistsException.class, () -> courseService.createCourse(course));

        assertEquals(ErrorsEnum.A201.message, exception.getMessage());
        assertEquals(ErrorsEnum.A201.code, exception.getCode());
    }

    @Test
    @DisplayName("Returns error when user does not exist")
    public void createCourse_ReturnsError_WhenUserDoesNotExist() throws UserNotFoundException{
        CourseModel course = CourseCreator.createCourseToBeSaved();

        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(userRepository.findRoleById(anyInt())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> courseService.createCourse(course));

        assertEquals(ErrorsEnum.A101.message, exception.getMessage());
        assertEquals(ErrorsEnum.A101.code, exception.getCode());
    }

    @Test
    @DisplayName("Returns error when user is not an instructor")
    public void createCourse_ReturnsError_WhenNotAnInstructor() throws InvalidRoleException {
        CourseModel course = CourseCreator.createCourseToBeSaved();

        when(courseRepository.existsByCode(anyString())).thenReturn(false);
        when(userRepository.findRoleById(anyInt())).thenReturn(Optional.of(RolesEnum.STUDENT.name()));

        InvalidRoleException exception = assertThrows(InvalidRoleException.class, () -> courseService.createCourse(course));

        assertEquals(ErrorsEnum.A202.message, exception.getMessage());
        assertEquals(ErrorsEnum.A202.code, exception.getCode());
    }

    @Test
    @DisplayName("Inactivates a course if code exists and current status is ACTIVE")
    public void deactivateCourse_ReturnsSuccessful_WhenCodeExistsAndStatusIsActive(){
        CourseModel course = CourseCreator.createValidCourse();
        when(courseRepository.findByCode(anyString())).thenReturn(Optional.of(course));

        courseService.deactivateCourse(course.getCode());

        course.setStatus(StatusEnum.INACTIVE.name());
        course.setInactivationDate(LocalDateTime.now());

        verify(courseRepository, times(1)).findByCode(anyString());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("Returns error if course does not exist")
    public void deactivateCourse_ReturnsError_WhenCourseDoesNotExist() throws CourseNotFoundException {
        when(courseRepository.findByCode(anyString())).thenReturn(Optional.empty());
        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.deactivateCourse("code"));

        assertEquals(ErrorsEnum.A203.message, exception.getMessage());
        assertEquals(ErrorsEnum.A203.code, exception.getCode());
    }

    @Test
    @DisplayName("Returns error if course is already inactive")
    public void deactivateCourse_ReturnsError_WhenCourseIsInactive() throws CourseAlreadyInactiveException {
        CourseModel course = CourseCreator.createInvalidCourse();
        when(courseRepository.findByCode(anyString())).thenReturn(Optional.of(course));
        CourseAlreadyInactiveException exception = assertThrows(CourseAlreadyInactiveException.class, () -> courseService.deactivateCourse(course.getCode()));

        assertEquals(ErrorsEnum.A204.message, exception.getMessage());
        assertEquals(ErrorsEnum.A204.code, exception.getCode());
    }

    @Test
    @DisplayName("Lists all courses")
    public void listCourses_ReturnsSuccessful_WhenCoursesExist()  {
        CourseModel activeCourse1 = CourseCreator.createValidCourse();
        CourseModel activeCourse2 = CourseCreator.createValidCourse();
        CourseModel inactiveCourse1 = CourseCreator.createValidCourse();
        CourseModel inactiveCourse2 = CourseCreator.createValidCourse();

        inactiveCourse1.setStatus(StatusEnum.INACTIVE.name());
        inactiveCourse2.setStatus(StatusEnum.INACTIVE.name());

        Pageable pageable = PageRequest.of(0, 10);

        Page<CourseModel> pageCourse = new PageImpl<>(List.of(activeCourse1, activeCourse2, inactiveCourse1, inactiveCourse2), pageable, 4);

        when(courseRepository.findAll(any(Pageable.class))).thenReturn(pageCourse);

        Page<GetCourseResponse> expected = pageCourse.map(obj -> GetCourseResponse.fromModel(obj));
        Page<GetCourseResponse> result = courseService.listCourses(0, 10, null);

        verify(courseRepository, times(1)).findAll(any(Pageable.class));
        assertTrue(expected.getTotalElements() == result.getTotalElements());
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);

    }

    @Test
    @DisplayName("Lists all courses by status")
    public void listCourses_ReturnsSuccessful_WhenStatusExists()  {
        CourseModel activeCourse1 = CourseCreator.createValidCourse();
        CourseModel activeCourse2 = CourseCreator.createValidCourse();

        Pageable pageable = PageRequest.of(0, 10);

        Page<CourseModel> pageCourse = new PageImpl<>(List.of(activeCourse1, activeCourse2), pageable, 2);

        when(courseRepository.findByStatus(anyString(), any(Pageable.class))).thenReturn(pageCourse);

        Page<GetCourseResponse> expected = pageCourse.map(obj -> GetCourseResponse.fromModel(obj));
        Page<GetCourseResponse> result = courseService.listCourses(0, 10, StatusEnum.ACTIVE);

        verify(courseRepository, times(1)).findByStatus(anyString(),any(Pageable.class));
        assertTrue(expected.getTotalElements() == result.getTotalElements());
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);

    }
}
