package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostCourseRequest;
import com.alura.coursecase.controllers.response.GetCourseResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.repositories.UserRepository;
import com.alura.coursecase.utils.CourseCreator;
import com.alura.coursecase.utils.UserCreator;
import com.alura.coursecase.utils.request.PostCourseRequestCreator;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setUp(){
        userRepository.save(UserCreator.createValidUser());
        userRepository.save(UserCreator.createValidInstructorUserToBeSaved());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Creates a course if payload is valid and user is an Admin")
    public void postCourse_ReturnsCreated_IfPayloadIsValidAndAdmin() throws Exception {
        PostCourseRequest request = PostCourseRequestCreator.createPostCourseRequestToBeSaved();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "notAdmin", roles={"STUDENT","INSTRUCTOR"})
    @DisplayName("Returns forbidden if user is not an Admin")
    public void postCourse_ReturnsForbidden_IfUserIsNotAnAdmin() throws Exception {
        PostCourseRequest request = PostCourseRequestCreator.createPostCourseRequestToBeSaved();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Returns unprocessable entity if payload is invalid")
    public void postCourse_ReturnsUnprocessableEntity_IfInvalidPayload() throws Exception {

        PostCourseRequest request = PostCourseRequestCreator.createInvalidFieldsPostCourseRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A002.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A002.code)))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Returns forbidden if code already exists")
    public void postCourse_ReturnsForbidden_IfCodeAlreadyExists() throws Exception {

        PostCourseRequest request = PostCourseRequestCreator.createPostCourseRequestToBeSaved();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A201.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A201.code)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Returns not found if instructor does not exist")
    public void postCourse_ReturnsNotFound_IfInstructorDoesNotExist() throws Exception {

        PostCourseRequest request = PostCourseRequestCreator.createInvalidUserPostCourseRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A101.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A101.code)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Returns forbidden if author is not an instructor does not exist")
    public void postCourse_ReturnsForbidden_IfInstructorDoesNotExist() throws Exception {

        PostCourseRequest request = PostCourseRequestCreator.createInvalidAuthorPostCourseRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A202.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A202.code)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Lists pageable courses and authenticated user is an Admin")
    public void listCourses_ReturnsOk_IfCoursesExistAndAdmin() throws Exception {
        GetCourseResponse response = GetCourseResponse.fromModel(CourseCreator.createValidCourse());

        mockMvc.perform(get("/course")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.pageable").exists())
                .andExpect(jsonPath("$.content[0].id", is(response.getId())))
                .andExpect(jsonPath("$.content[0].name", is(response.getName())))
                .andExpect(jsonPath("$.content[0].code", is(response.getCode())))
                .andExpect(jsonPath("$.content[0].instructor.name", is(response.getInstructor().getName())))
                .andExpect(jsonPath("$.content[0].instructor.username", is(response.getInstructor().getUsername())))
                .andExpect(jsonPath("$.content[0].instructor.email", is(response.getInstructor().getEmail())))
                .andExpect(jsonPath("$.content[0].description", is(response.getDescription())))
                .andExpect(jsonPath("$.content[0].status", is(response.getStatus())))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Lists pageable with empty list of courses and authenticated user is an Admin if none match the Status query parameter")
    public void listCourses_ReturnsOkWithEmptyList_IfNoCoursesWithStatusAndAdmin() throws Exception {
        GetCourseResponse response = GetCourseResponse.fromModel(CourseCreator.createValidCourse());

        mockMvc.perform(get("/course").param("status",StatusEnum.INACTIVE.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0]").doesNotExist())
                .andExpect(jsonPath("$.pageable").exists())
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithMockUser(username = "notAdmin", roles={"INSTRUCTOR","STUDENT"})
    @DisplayName("Returns forbidden if user authenticated is not an Admin")
    public void listCourses_ReturnsForbidden_IfUserIsNotAnAdmin() throws Exception {
        mockMvc.perform(get("/course"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Deactivates a course if payload is valid and user is an Admin")
    public void deactivateCourse_ReturnsOk_IfCourseWasActiveAndAdmin() throws Exception {
        PostCourseRequest request = PostCourseRequestCreator.createPostCourseRequestToBeSaved();

        mockMvc.perform(patch("/course/deactivate/{code}", request.getCode()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(11)
    @WithMockUser(username = "notAdmin", roles={"INSTRUCTOR","STUDENT"})
    @DisplayName("Returns forbidden if user authenticated is not an Admin")
    public void deactivateCourse_ReturnsForbidden_IfUserIsNotAnAdmin() throws Exception {
        PostCourseRequest request = PostCourseRequestCreator.createPostCourseRequestToBeSaved();

        mockMvc.perform(patch("/course/deactivate/{code}", request.getCode()))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(12)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Returns not found if course does not exist")
    public void deactivateCourse_ReturnsNotFound_IfCourseDoesNotExist() throws Exception {

        mockMvc.perform(patch("/course/deactivate/{code}", "invalid"))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A203.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A203.code)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(13)
    @WithMockUser(username = "admin", roles={"ADMIN"})
    @DisplayName("Returns forbidden if course is already inactive")
    public void deactivateCourse_ReturnForbidden_IfCourseIsAlreadyInactive() throws Exception {
        PostCourseRequest request = PostCourseRequestCreator.createPostCourseRequestToBeSaved();

        mockMvc.perform(patch("/course/deactivate/{code}", request.getCode()))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A204.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A204.code)))
                .andExpect(status().isForbidden());
    }


}
