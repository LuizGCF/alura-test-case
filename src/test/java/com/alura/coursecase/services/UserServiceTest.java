package com.alura.coursecase.services;

import com.alura.coursecase.controllers.response.GetUserResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.exception.UserAlreadyExistsException;
import com.alura.coursecase.exception.UserNotFoundException;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.repositories.UserRepository;
import com.alura.coursecase.utils.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Creates an user when payload is valid")
    public void createUser_ReturnsSuccessful_WhenValid() {
        UserModel user = UserCreator.createUserToBeSaved();
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        userService.createUser(user);

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Returns error when username already exists")
    public void createUser_ReturnsError_WhenUsernameExists() throws UserAlreadyExistsException{
        UserModel user = UserCreator.createUserToBeSaved();

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));

        assertEquals(ErrorsEnum.A102.message.formatted("username"), exception.getMessage());
        assertEquals(ErrorsEnum.A102.code, exception.getCode());

        verify(userRepository, times(0)).existsByEmail(anyString());
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    @DisplayName("Returns error when email already exists")
    public void createUser_ReturnsError_WhenEmailExists() throws UserAlreadyExistsException{
        UserModel user = UserCreator.createUserToBeSaved();

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));

        assertEquals(ErrorsEnum.A102.message.formatted("email"), exception.getMessage());
        assertEquals(ErrorsEnum.A102.code, exception.getCode());

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(0)).save(user);
    }

    @Test
    @DisplayName("Finds an user by username when user exists")
    public void findUserByUsername_ReturnsAnUser_WhenUserExists(){
        UserModel user = UserCreator.createValidUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        GetUserResponse response = userService.findUserByUsername("john");

        verify(userRepository, times(1)).findByUsername(anyString());
        assertThat(response).usingRecursiveComparison().isEqualTo(GetUserResponse.fromModel(user));
    }

    @Test
    @DisplayName("Returns error when user does not exist")
    public void findUserByUsername_ReturnsError_WhenUserDoesNotExist() throws UserNotFoundException {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findUserByUsername("johnny"));

        assertEquals(ErrorsEnum.A101.message, exception.getMessage());
        assertEquals(ErrorsEnum.A101.code, exception.getCode());

        verify(userRepository, times(1)).findByUsername(anyString());
    }
}
