package com.alura.coursecase.services;

import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.exception.CourseAlreadyInactiveException;
import com.alura.coursecase.exception.CourseNotFoundException;
import com.alura.coursecase.exception.UserAlreadyRegisteredException;
import com.alura.coursecase.exception.UserNotFoundException;
import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.models.RegistrationModel;
import com.alura.coursecase.repositories.CourseRepository;
import com.alura.coursecase.repositories.RegistrationRepository;
import com.alura.coursecase.repositories.UserRepository;
import com.alura.coursecase.utils.CourseCreator;
import com.alura.coursecase.utils.RegistrationCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RegistrationServiceTest {

    @InjectMocks
    RegistrationService registrationService;

    @Mock
    RegistrationRepository registrationRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CourseRepository courseRepository;

    @Test
    @DisplayName("Creates a registration when payload is valid")
    public void register_ReturnsSuccessful_WhenValid() {
        RegistrationModel registration = RegistrationCreator.createRegistrationToBeSaved();
        CourseModel course = CourseCreator.createValidCourse();

        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(course));
        when(registrationRepository.existsByIdUserAndIdCourse(anyInt(), anyInt())).thenReturn(false);

        registrationService.register(registration);

        verify(userRepository, times(1)).existsById(anyInt());
        verify(courseRepository, times(1)).findById(anyInt());
        verify(registrationRepository, times(1)).existsByIdUserAndIdCourse(anyInt(), anyInt());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    @DisplayName("Returns error when user does not exist")
    public void register_ReturnsError_WhenUserDoesNotExist() throws UserNotFoundException{
        RegistrationModel registration = RegistrationCreator.createInvalidRegistration();

        when(userRepository.existsById(anyInt())).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> registrationService.register(registration));

        assertEquals(ErrorsEnum.A101.message, exception.getMessage());
        assertEquals(ErrorsEnum.A101.code, exception.getCode());

        verify(userRepository, times(1)).existsById(anyInt());
        verify(courseRepository, times(0)).findById(anyInt());
        verify(registrationRepository, times(0)).existsByIdUserAndIdCourse(anyInt(), anyInt());
        verify(registrationRepository, times(0)).save(registration);
    }

    @Test
    @DisplayName("Returns error when course does not exist")
    public void register_ReturnsError_WhenCourseDoesNotExist() throws CourseNotFoundException {
        RegistrationModel registration = RegistrationCreator.createInvalidRegistration();

        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(courseRepository.findById(anyInt())).thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> registrationService.register(registration));

        assertEquals(ErrorsEnum.A203.message, exception.getMessage());
        assertEquals(ErrorsEnum.A203.code, exception.getCode());

        verify(userRepository, times(1)).existsById(anyInt());
        verify(courseRepository, times(1)).findById(anyInt());
        verify(registrationRepository, times(0)).existsByIdUserAndIdCourse(anyInt(), anyInt());
        verify(registrationRepository, times(0)).save(registration);
    }

    @Test
    @DisplayName("Returns error when course is inactive")
    public void register_ReturnsError_WhenCourseIsInactive() throws CourseAlreadyInactiveException {
        RegistrationModel registration = RegistrationCreator.createInvalidRegistration();
        CourseModel course = CourseCreator.createInvalidCourse();

        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(course));

        CourseAlreadyInactiveException exception = assertThrows(CourseAlreadyInactiveException.class, () -> registrationService.register(registration));

        assertEquals(ErrorsEnum.A204.message, exception.getMessage());
        assertEquals(ErrorsEnum.A204.code, exception.getCode());

        verify(userRepository, times(1)).existsById(anyInt());
        verify(courseRepository, times(1)).findById(anyInt());
        verify(registrationRepository, times(0)).existsByIdUserAndIdCourse(anyInt(), anyInt());
        verify(registrationRepository, times(0)).save(registration);
    }

    @Test
    @DisplayName("Returns error when registration already exists")
    public void register_ReturnsError_WhenRegistrationAlreadyExists() throws UserAlreadyRegisteredException {
        RegistrationModel registration = RegistrationCreator.createRegistrationToBeSaved();
        CourseModel course = CourseCreator.createValidCourse();

        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(course));
        when(registrationRepository.existsByIdUserAndIdCourse(anyInt(), anyInt())).thenReturn(true);

        UserAlreadyRegisteredException exception = assertThrows(UserAlreadyRegisteredException.class, () -> registrationService.register(registration));

        verify(userRepository, times(1)).existsById(anyInt());
        verify(courseRepository, times(1)).findById(anyInt());
        verify(registrationRepository, times(1)).existsByIdUserAndIdCourse(anyInt(), anyInt());
        verify(registrationRepository, times(0)).save(registration);

        assertEquals(ErrorsEnum.A301.message, exception.getMessage());
        assertEquals(ErrorsEnum.A301.code, exception.getCode());
    }
}
