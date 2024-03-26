package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostUserRequest;
import com.alura.coursecase.controllers.response.GetUserResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.utils.UserCreator;
import com.alura.coursecase.utils.request.PostUserRequestCreator;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("Creates a user if payload is valid")
    public void postUser_ReturnsCreated_IfPayloadIsValid() throws Exception {

        PostUserRequest postUserRequest = PostUserRequestCreator.createPostUserRequestToBeSaved();

        var json = new ObjectMapper().writeValueAsString(postUserRequest);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("Returns forbidden if username already exists")
    public void postUser_ReturnsForbidden_IfUsernameAlreadyExists() throws Exception {

        PostUserRequest postUserRequest = PostUserRequestCreator.createPostUserRequestToBeSaved();

        var json = new ObjectMapper().writeValueAsString(postUserRequest);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A102.message.formatted("username"))))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A102.code)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @DisplayName("Returns forbidden if email already exists")
    public void postUser_ReturnsForbidden_IfEmailAlreadyExists() throws Exception {

        PostUserRequest postUserRequest = PostUserRequestCreator.createPostUserRequestToBeSaved();
        postUserRequest.setUsername("johnny");

        var json = new ObjectMapper().writeValueAsString(postUserRequest);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A102.message.formatted("email"))))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A102.code)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    @DisplayName("Returns unprocessable entity if payload is invalid")
    public void postUser_ReturnsUnprocessableEntity_IfInvalidPayload() throws Exception {

        PostUserRequest postUserRequest = PostUserRequestCreator.createInvalidPostUserRequest();

        var json = new ObjectMapper().writeValueAsString(postUserRequest);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A002.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A002.code)))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Returns an user if username exists and authenticated user is an Admin")
    public void getUserByUsername_ReturnsOk_IfUsernameExistsAndAdminRole() throws Exception {
        UserModel userModel = UserCreator.createValidUser();
        GetUserResponse getUserResponse = GetUserResponse.fromModel(userModel);

        var json = new ObjectMapper().writeValueAsString(getUserResponse);

        mockMvc.perform(get("/user/{username}", userModel.getUsername()))
                .andExpect(content().json(json))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "notAdmin", roles={"INSTRUCTOR","STUDENT"})
    @DisplayName("Returns forbidden if user authenticated is not an Admin")
    public void getUserByUsername_ReturnsForbidden_IfUserIsNotAnAdmin() throws Exception {
        mockMvc.perform(get("/user/{username}", "username"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Returns not found if username does not exist")
    public void getUserByUsername_ReturnsNotFound_IfUsernameDoesNotExist() throws Exception {

        mockMvc.perform(get("/user/{username}", "notfound"))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A101.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A101.code)))
                .andExpect(status().isNotFound());
    }
}
