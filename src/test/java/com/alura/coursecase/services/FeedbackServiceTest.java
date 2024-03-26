package com.alura.coursecase.services;

import com.alura.coursecase.controllers.response.GetCoursesReportResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.exception.*;
import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.models.FeedbackModel;
import com.alura.coursecase.models.RegistrationModel;
import com.alura.coursecase.repositories.FeedbackRepository;
import com.alura.coursecase.repositories.RegistrationRepository;
import com.alura.coursecase.utils.FeedbackCreator;
import com.alura.coursecase.utils.RegistrationCreator;
import com.alura.coursecase.utils.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FeedbackServiceTest {

    @InjectMocks
    FeedbackService feedbackService;

    @Mock
    FeedbackRepository feedbackRepository;

    @Mock
    RegistrationRepository registrationRepository;

    private List<RegistrationModel> createRegistrationList(int startingRegistrationId, int startingFeedbackId, int courseId, boolean withFeedback, int... rating) {
        List<RegistrationModel> response = new ArrayList<>();
        CourseModel c = new CourseModel(courseId, UserCreator.createValidInstructorUser(), "Course %d".formatted(courseId), "code", "description", StatusEnum.ACTIVE.name(), LocalDateTime.now(), null);
        for (int i = 1; i <= rating.length; i++) {
            FeedbackModel f = new FeedbackModel(startingFeedbackId + i, "title", "description", rating[i - 1], LocalDateTime.now(), null);
            RegistrationModel r = new RegistrationModel(startingRegistrationId + i, UserCreator.createValidUser(), c, LocalDateTime.now(), withFeedback ? f : null);

            response.add(r);
        }
        return response;
    }

    @Test
    @DisplayName("Send feeback when payload is valid")
    public void sendFeeback_ReturnsSuccessful_WhenValid() {
        FeedbackModel feedbackModel = FeedbackCreator.createFeedbackToBeSaved();
        FeedbackModel feedbackSaved = FeedbackCreator.createValidFeedback();
        RegistrationModel registration = RegistrationCreator.createValidRegistrationForFeedbackSending();
        RegistrationModel updatedRegistration = registration;

        when(registrationRepository.findById(anyInt())).thenReturn(Optional.of(registration));
        when(feedbackRepository.save(any(FeedbackModel.class))).thenReturn(feedbackSaved);

        feedbackService.sendFeedback(feedbackModel);

        verify(registrationRepository, times(1)).findById(anyInt());
        verify(feedbackRepository, times(1)).save(any());
        verify(registrationRepository, times(1)).save(updatedRegistration);
    }

    @Test
    @DisplayName("Send feeback and send email when payload is valid and rating is lower than six")
    public void sendFeeback_ReturnsSuccessfulAndSensEmail_WhenRatingLowerThanSix() {
        FeedbackModel feedbackModel = FeedbackCreator.createFeedbackToBeSavedWithLowRating();
        FeedbackModel feedbackSaved = FeedbackCreator.createValidFeedbackWithLowRating();
        RegistrationModel registration = RegistrationCreator.createValidRegistrationForFeedbackSending();
        RegistrationModel updatedRegistration = registration;

        when(registrationRepository.findById(anyInt())).thenReturn(Optional.of(registration));
        when(feedbackRepository.save(any(FeedbackModel.class))).thenReturn(feedbackSaved);

        feedbackService.sendFeedback(feedbackModel);

        verify(registrationRepository, times(1)).findById(anyInt());
        verify(feedbackRepository, times(1)).save(any());
        verify(registrationRepository, times(1)).save(updatedRegistration);
    }

    @Test
    @DisplayName("Returns error when no title or description with rating lower than six")
    public void sendFeeback_ReturnsError_WhenInvalidPayloadForRating() throws InvalidFeedbackObjectException {
        FeedbackModel feedback = FeedbackCreator.createInvalidFeedback();

        InvalidFeedbackObjectException exception = assertThrows(InvalidFeedbackObjectException.class, () -> feedbackService.sendFeedback(feedback));

        assertEquals(ErrorsEnum.A403.message, exception.getMessage());
        assertEquals(ErrorsEnum.A403.code, exception.getCode());

        verify(registrationRepository, times(0)).findById(anyInt());
        verify(feedbackRepository, times(0)).save(any());
        verify(registrationRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Returns error when registration does not exist")
    public void sendFeeback_ReturnsError_WhenRegistrationDoesNotExist() throws RegistrationNotFoundException {
        FeedbackModel feedback = FeedbackCreator.createValidFeedback();

        when(registrationRepository.findById(anyInt())).thenReturn(Optional.empty());

        RegistrationNotFoundException exception = assertThrows(RegistrationNotFoundException.class, () -> feedbackService.sendFeedback(feedback));

        assertEquals(ErrorsEnum.A302.message, exception.getMessage());
        assertEquals(ErrorsEnum.A302.code, exception.getCode());

        verify(registrationRepository, times(1)).findById(anyInt());
        verify(feedbackRepository, times(0)).save(any());
        verify(registrationRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Returns error when feedback already exists")
    public void sendFeeback_ReturnsError_WhenFeedbackAlreadyExists() throws FeedbackAlreadySentException {
        RegistrationModel registration = RegistrationCreator.createValidRegistrationWithFeedback();
        FeedbackModel feedback = FeedbackCreator.createValidFeedback();

        when(registrationRepository.findById(anyInt())).thenReturn(Optional.of(registration));

        FeedbackAlreadySentException exception = assertThrows(FeedbackAlreadySentException.class, () -> feedbackService.sendFeedback(feedback));

        assertEquals(ErrorsEnum.A401.message, exception.getMessage());
        assertEquals(ErrorsEnum.A401.code, exception.getCode());

        verify(registrationRepository, times(1)).findById(anyInt());
        verify(feedbackRepository, times(0)).save(any());
        verify(registrationRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Returns error when course is inactive")
    public void sendFeeback_ReturnsError_WhenCourseIsInactive() throws CourseAlreadyInactiveException {
        RegistrationModel registration = RegistrationCreator.createInvalidRegistration();
        FeedbackModel feedback = FeedbackCreator.createValidFeedback();

        when(registrationRepository.findById(anyInt())).thenReturn(Optional.of(registration));

        CourseAlreadyInactiveException exception = assertThrows(CourseAlreadyInactiveException.class, () -> feedbackService.sendFeedback(feedback));

        assertEquals(ErrorsEnum.A204.message, exception.getMessage());
        assertEquals(ErrorsEnum.A204.code, exception.getCode());

        verify(registrationRepository, times(1)).findById(anyInt());
        verify(feedbackRepository, times(0)).save(any());
        verify(registrationRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Lists a report with all courses with 4 or more registrations and their respective NPS")
    public void listCoursesReport_ReturnsSuccessful_WhenCoursesHaveRegistrations() {
        GetCoursesReportResponse report1 = new GetCoursesReportResponse(1, "Course 1", 4L, 4, -75f, StatusEnum.ACTIVE.name());
        GetCoursesReportResponse report2 = new GetCoursesReportResponse(2, "Course 2", 5L, 5, 60f, StatusEnum.ACTIVE.name());
        GetCoursesReportResponse report3 = new GetCoursesReportResponse(3, "Course 3", 4L, 0, 0f, StatusEnum.ACTIVE.name());
        List<GetCoursesReportResponse> expected = List.of(report1, report2, report3);

        List<RegistrationModel> registrationModelList = new ArrayList<>();
        registrationModelList.addAll(createRegistrationList(0, 0, 1, true, 6, 5, 5, 4));
        registrationModelList.addAll(createRegistrationList(5, 5, 2, true, 9, 10, 10, 5, 9));
        registrationModelList.addAll(createRegistrationList(10, 10, 3, false, 0, 0, 0, 0));

        List<FeedbackModel> feedbackModelList = registrationModelList.stream()
                .map(RegistrationModel::getFeedback)
                .filter(feedback -> feedback != null)
                .collect(Collectors.toList());

        when(registrationRepository.findByIdCourseGreaterThanThreeOcurrences()).thenReturn(Optional.of(registrationModelList));
        when(feedbackRepository.findAllById(any())).thenReturn(feedbackModelList);

        List<GetCoursesReportResponse> result = feedbackService.listCoursesReport();

        verify(registrationRepository, times(1)).findByIdCourseGreaterThanThreeOcurrences();
        verify(feedbackRepository, times(1)).findAllById(any());
        assertEquals(expected.size(), result.size());
        assertThat(expected.equals(result));

    }

    @Test
    @DisplayName("Returns error when registration does not exist")
    public void listCoursesReport_ReturnsError_WhenRegistrationDoesNotExist() throws FeedbackNotFoundException {

        when(registrationRepository.findByIdCourseGreaterThanThreeOcurrences()).thenReturn(Optional.empty());

        FeedbackNotFoundException exception = assertThrows(FeedbackNotFoundException.class, () -> feedbackService.listCoursesReport());

        assertEquals(ErrorsEnum.A402.message, exception.getMessage());
        assertEquals(ErrorsEnum.A402.code, exception.getCode());

        verify(registrationRepository, times(1)).findByIdCourseGreaterThanThreeOcurrences();
        verify(feedbackRepository, times(0)).findAllById(any());

    }

}
