package com.alura.coursecase.services;

import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.exception.UserNotFoundException;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.repositories.UserRepository;
import com.alura.coursecase.utils.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorizationServiceTest {

    @InjectMocks
    AuthorizationService authorizationService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("Finds an user by username when user exists")
    public void findUserByUsername_ReturnsAnUser_WhenUserExists(){
        UserModel user = UserCreator.createValidUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserDetails response = authorizationService.loadUserByUsername("john");

        verify(userRepository, times(1)).findByUsername(anyString());
        assertThat(response).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    @DisplayName("Returns error when user does not exist")
    public void findUserByUsername_ReturnsError_WhenUserDoesNotExist() throws UserNotFoundException {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> authorizationService.loadUserByUsername("johnny"));

        assertEquals(ErrorsEnum.A101.message, exception.getMessage());
        assertEquals(ErrorsEnum.A101.code, exception.getCode());
    }
}
