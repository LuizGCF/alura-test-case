package com.alura.coursecase.services;

import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.exception.InvalidTokenException;
import com.alura.coursecase.exception.TokenGenerationFailedException;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.utils.UserCreator;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TokenServiceTest {

    @InjectMocks
    TokenService tokenService;

    @BeforeEach
    public void before(){
        ReflectionTestUtils.setField(tokenService, "secret", "secret");
    }

    @Test
    @DisplayName("Generates a token if password was provided")
    public void generateToken_ReturnsToken_IfPasswordProvided(){
        UserModel user = UserCreator.createValidUser();
        String generatedToken = tokenService.generateToken(user);

        DecodedJWT decodedToken = JWT.decode(generatedToken);

        assertEquals(user.getUsername(), decodedToken.getSubject());
        assertEquals("alura-course-case", decodedToken.getIssuer());
    }


    @Test
    @DisplayName("Returns error if token can't be generated")
    public void generateToken_ReturnsError_IfTokenCantBeGenerated() throws TokenGenerationFailedException {
        ReflectionTestUtils.setField(tokenService, "secret", null);
        UserModel user = UserCreator.createValidUser();
        TokenGenerationFailedException exception = assertThrows(TokenGenerationFailedException.class, () -> tokenService.generateToken(user));

        assertEquals(ErrorsEnum.A001.message, exception.getMessage());
        assertEquals(ErrorsEnum.A001.code, exception.getCode());
    }

    @Test
    @DisplayName("Returns the username if token is valid")
    public void validateToken_ReturnsUsername_IfValidToken(){
        UserModel user = UserCreator.createValidUser();
        String generatedToken = tokenService.generateToken(user);

        String username = tokenService.validateToken(generatedToken);

        assertEquals(user.getUsername(), username);
    }

    @Test
    @DisplayName("Returns error if token can't be validated")
    public void validateToken_ReturnsError_IfTokenCantBeValidated() throws InvalidTokenException {
        String token = "token";
        InvalidTokenException exception = assertThrows(InvalidTokenException.class, () -> tokenService.validateToken(token));

        assertEquals(ErrorsEnum.A000.message, exception.getMessage());
        assertEquals(ErrorsEnum.A000.code, exception.getCode());
    }
}
