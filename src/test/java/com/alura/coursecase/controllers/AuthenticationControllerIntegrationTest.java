package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.AuthenticationRequest;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.repositories.UserRepository;
import com.alura.coursecase.utils.UserCreator;
import com.alura.coursecase.utils.request.AuthenticationRequestCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeAll
    public void setUp(){
        userRepository.save(UserCreator.createValidUser());
    }

    @Test
    @Order(1)
    @DisplayName("Authenticate an user if credentials exist")
    public void postLogin_ReturnsToken_IfPayloadIsValid() throws Exception {
        AuthenticationRequest request = AuthenticationRequestCreator.createValidAuthenticationRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("Returns not found if user does not exist")
    public void postLogin_ReturnsNotFound_IfUserDoesNotExist() throws Exception {

        AuthenticationRequest request = AuthenticationRequestCreator.createInvalidAuthenticationRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A101.code)))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A101.message)))
                .andExpect(status().isNotFound());
    }
}
