package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostRegistrationRequest;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.repositories.CourseRepository;
import com.alura.coursecase.repositories.UserRepository;
import com.alura.coursecase.utils.CourseCreator;
import com.alura.coursecase.utils.UserCreator;
import com.alura.coursecase.utils.request.PostRegistrationRequestCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
public class RegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeAll
    public void setUp() {
        userRepository.save(UserCreator.createValidUser());
        userRepository.save(UserCreator.createValidInstructorUserToBeSaved());
        courseRepository.save(CourseCreator.createCourseToBeSaved());
        courseRepository.save(CourseCreator.createInactiveCourse());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Creates a registration if payload is valid and user is authenticated")
    public void postRegistration_ReturnsCreated_IfPayloadIsValidAndAuthenticated() throws Exception {
        PostRegistrationRequest request = PostRegistrationRequestCreator.createPostRegistrationRequestToBeSaved();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("Returns forbidden if user is not authenticated")
    public void postRegistration_ReturnsForbidden_IfUserIsNotAuthenticated() throws Exception {
        PostRegistrationRequest request = PostRegistrationRequestCreator.createPostRegistrationRequestToBeSaved();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns not found if user does not exist")
    public void postRegistration_ReturnsNotFound_IfUserDoesNotExist() throws Exception {
        PostRegistrationRequest request = PostRegistrationRequestCreator.createInvalidUserPostRegistrationRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A101.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A101.code)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns not found if course does not exist")
    public void postRegistration_ReturnsNotFound_IfCourseDoesNotExist() throws Exception {
        PostRegistrationRequest request = PostRegistrationRequestCreator.createInvalidCoursePostRegistrationRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A203.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A203.code)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns forbidden if course is already inactive")
    public void postRegistration_ReturnsForbidden_IfCourseIsInactive() throws Exception {
        PostRegistrationRequest request = PostRegistrationRequestCreator.createInactiveCoursePostRegistrationRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A204.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A204.code)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns forbidden if user is already registered to the course")
    public void postRegistration_ReturnsForbidden_IfUserAlreadyRegistered() throws Exception {
        PostRegistrationRequest request = PostRegistrationRequestCreator.createPostRegistrationRequestToBeSaved();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A301.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A301.code)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(7)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns unprocessable entity if payload is invalid")
    public void postRegistration_ReturnsUnprocessableEntity_IfInvalidPayload() throws Exception {

        PostRegistrationRequest request = PostRegistrationRequestCreator.createInvalidFieldsPostRegistrationRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A002.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A002.code)))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(status().isUnprocessableEntity());
    }
}
