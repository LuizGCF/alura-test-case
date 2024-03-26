package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostFeedbackRequest;
import com.alura.coursecase.controllers.response.GetCoursesReportResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.enums.RolesEnum;
import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.models.FeedbackModel;
import com.alura.coursecase.models.RegistrationModel;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.repositories.CourseRepository;
import com.alura.coursecase.repositories.FeedbackRepository;
import com.alura.coursecase.repositories.RegistrationRepository;
import com.alura.coursecase.repositories.UserRepository;
import com.alura.coursecase.utils.CourseCreator;
import com.alura.coursecase.utils.RegistrationCreator;
import com.alura.coursecase.utils.UserCreator;
import com.alura.coursecase.utils.request.PostFeedbackRequestCreator;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FeedbackControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @BeforeAll
    public void setUp() {
        userRepository.save(UserCreator.createValidUser());
        userRepository.save(UserCreator.createValidInstructorUserToBeSaved());

        courseRepository.save(CourseCreator.createCourseToBeSaved());
        courseRepository.save(CourseCreator.createInactiveCourse());

        registrationRepository.save(RegistrationCreator.createRegistrationToBeSaved());
        registrationRepository.save(RegistrationCreator.createInactiveCourseRegistration());

    }

    @Test
    @Order(1)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Send feeback if payload is valid and user is authenticated")
    public void sendFeedback_ReturnsCreated_IfPayloadIsValidAndAuthenticated() throws Exception {
        PostFeedbackRequest request = PostFeedbackRequestCreator.createPostFeedbackRequestWithGoodRatingToBeSaved();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns unprocessable entity if title and/or description are not filled with rating lower than six")
    public void sendFeedback_ReturnsUnprocessableEntity_IfMissingFieldsBasedOnRating() throws Exception {
        PostFeedbackRequest request = PostFeedbackRequestCreator.createInvalidPostFeedbackRequestWithBadRating();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A403.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A403.code)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns not found if registration does not exist")
    public void sendFeedback_ReturnsNotFound_IfRegistrationDoesNotExist() throws Exception {
        PostFeedbackRequest request = PostFeedbackRequestCreator.createInvalidRegistrationPostFeedbackRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A302.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A302.code)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns forbidden if feedback was already sent")
    public void sendFeedback_ReturnsForbidden_IfFeedbackWasAlreadySent() throws Exception {
        PostFeedbackRequest request = PostFeedbackRequestCreator.createAlreadySentFeedbackPostFeedbackRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A401.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A401.code)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns forbidden if course is already inactive")
    public void sendFeedback_ReturnsForbidden_IfCourseIsAlreadyInactive() throws Exception {
        PostFeedbackRequest request = PostFeedbackRequestCreator.createInactiveCoursePostFeedbackRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A204.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A204.code)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(6)
    @WithMockUser(username = "authenticated", roles = {"ADMIN", "INSTRUCTOR", "STUDENT"})
    @DisplayName("Returns unprocessable entity if payload is invalid")
    public void sendFeedback_ReturnsUnprocessableEntity_IfPayloadIsInvalid() throws Exception {
        PostFeedbackRequest request = PostFeedbackRequestCreator.createInvalidFieldsPostFeedbackRequest();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A002.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A002.code)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Order(7)
    @DisplayName("Returns forbidden if user is not authenticated")
    public void sendFeedback_ReturnsForbidden_IfNotAuthenticated() throws Exception {
        PostFeedbackRequest request = PostFeedbackRequestCreator.createPostFeedbackRequestWithGoodRatingToBeSaved();

        var json = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
    }


    @Test
    @Order(8)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Lists courses NPS if user is an Admin")
    public void listCoursesReport_ReturnsOk_IfCoursesExistAndAdmin() throws Exception {
        GetCoursesReportResponse response = setNPSData();

        mockMvc.perform(get("/feedback/report")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idCourse", is(response.getIdCourse())))
                .andExpect(jsonPath("$[0].courseName", is(response.getCourseName())))
                .andExpect(jsonPath("$[0].totalRegistrations", is(Math.toIntExact(response.getTotalRegistrations()))))
                .andExpect(jsonPath("$[0].totalFeedbacks", is(response.getTotalFeedbacks())))
                .andExpect(jsonPath("$[0].nps", is((double) response.getNps())))
                .andExpect(jsonPath("$[0].status", is(response.getStatus())))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithMockUser(username = "notAdmin", roles = {"STUDENT", "INSTRUCTOR"})
    @DisplayName("Returns forbidden if user is not an Admin")
    public void listCoursesReport_ReturnsForbidden_IfNotAnAdmin() throws Exception {
        mockMvc.perform(get("/feedback/report"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
    @DisplayName("Returns forbidden if user is not authenticated")
    public void listCoursesReport_ReturnsForbidden_IfNotAuthenticated() throws Exception {
        mockMvc.perform(get("/feedback/report"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(11)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Returns not found if not enough feedbacks on any courses")
    public void listCoursesReport_ReturnsNotFound_IfNotEnoughFeedback() throws Exception {
        registrationRepository.deleteAll();

        mockMvc.perform(get("/feedback/report"))
                .andExpect(jsonPath("$.message", is(ErrorsEnum.A402.message)))
                .andExpect(jsonPath("$.code", is(ErrorsEnum.A402.code)))
                .andExpect(status().isNotFound());
    }

    private GetCoursesReportResponse setNPSData() {
        List<String> usernames = Arrays.asList("first", "second", "third", "fourth");
        CourseModel course = new CourseModel(null, UserCreator.createValidInstructorUser(), "Course NPS", "nps", "nps course", StatusEnum.ACTIVE.name(), LocalDateTime.now(), null);
        course = courseRepository.save(course);

        for (int i = 1; i <= 4; i++) {
            UserModel user = new UserModel(null, "name %d".formatted(i), usernames.get(i - 1), "email@email%d.com".formatted(i), "password", RolesEnum.STUDENT.name(), LocalDateTime.now());
            userRepository.save(user);

            FeedbackModel feedback = new FeedbackModel(null, "feedback %d".formatted(i), "description %d".formatted(i), 10, LocalDateTime.now(), null);
            feedbackRepository.save(feedback);

            RegistrationModel r = new RegistrationModel(null, user, course, LocalDateTime.now(), feedback);
            registrationRepository.save(r);
        }
        return new GetCoursesReportResponse(course.getId(), course.getName(), 4L, 4, 100f, course.getStatus());

    }

}
