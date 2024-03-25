package com.alura.coursecase.services;

import com.alura.coursecase.controllers.response.GetCoursesReportResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.exception.*;
import com.alura.coursecase.models.CourseModel;
import com.alura.coursecase.models.FeedbackModel;
import com.alura.coursecase.models.RegistrationModel;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.repositories.FeedbackRepository;
import com.alura.coursecase.repositories.RegistrationRepository;
import com.alura.coursecase.utils.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class FeedbackService {

    private static final int UNSATISFACTORY_RATING = 5;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    public void sendFeedback(FeedbackModel feedbackModel) {
        if(!isFeedbackValid(feedbackModel))
            throw new InvalidFeedbackObjectException(ErrorsEnum.A403.code, ErrorsEnum.A403.message);

        Optional<RegistrationModel> optionalRegistration = registrationRepository.findById(feedbackModel.getIdRegistration());
        if (optionalRegistration.isEmpty())
            throw new RegistrationNotFoundException(ErrorsEnum.A302.code, ErrorsEnum.A302.message);

        RegistrationModel registration = optionalRegistration.get();

        if (Objects.nonNull(registration.getFeedback()))
            throw new FeedbackAlreadySentException(ErrorsEnum.A401.code, ErrorsEnum.A401.message);

        CourseModel course = registration.getCourse();

        if (course.getStatus().equals(StatusEnum.INACTIVE.name()))
            throw new CourseAlreadyInactiveException(ErrorsEnum.A204.code, ErrorsEnum.A204.message);

        FeedbackModel feedbackSaved = feedbackRepository.save(feedbackModel);

        registration.setFeedback(feedbackSaved);
        registrationRepository.save(registration);

        if (feedbackModel.getRating() <= UNSATISFACTORY_RATING) {
            UserModel instructor = course.getInstructor();
            EmailSender.send(instructor.getEmail(), instructor.getName(), course.getName(), feedbackModel.getDescription());
        }

    }

    private boolean isFeedbackValid(FeedbackModel feedbackModel) {
        if(feedbackModel.getRating() >= 6)
            return true;
        else if(Objects.isNull(feedbackModel.getTitle()) || feedbackModel.getTitle().isEmpty())
            return false;
        else return !Objects.isNull(feedbackModel.getDescription()) && !feedbackModel.getDescription().isEmpty();
    }

    public List<GetCoursesReportResponse> listCoursesReport() {
        Optional<List<RegistrationModel>> optionalRegistrations = registrationRepository.findByIdCourseGreaterThanThreeOcurrences();
        if (optionalRegistrations.isEmpty())
            throw new FeedbackNotFoundException(ErrorsEnum.A402.code, ErrorsEnum.A402.message);

        List<RegistrationModel> registrations = optionalRegistrations.get();

        //Mapping courses where the key is the course ID and the value its corresponding object
        Map<Integer, CourseModel> coursesMap = registrations.stream()
                .collect(Collectors.toMap(
                        registration -> registration.getCourse().getId(),
                        RegistrationModel::getCourse,
                        (existing, replacement) -> existing
                ));

        //Mapping courses where the key is the course ID and the value its total registrations
        Map<Integer, Long> mapCoursesRegistrationCount = registrations.stream().map(r -> r.getCourse())
                .collect(collectingAndThen(groupingBy(c -> c.getId(), counting()),
                        map -> map.entrySet().stream()
                                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
                ));

        //Mapping courses where the key is the course ID and the value a list of its feedback IDs
        Map<Integer, List<Integer>> mapCoursesFeedbacks = registrations.stream()
                .collect(Collectors.groupingBy(r -> r.getCourse().getId(),
                        Collectors.mapping(r -> {
                            FeedbackModel feedback = r.getFeedback();
                            return feedback != null ? feedback.getId() : null;
                        }, Collectors.filtering(Objects::nonNull, Collectors.toList()))));

        List<FeedbackModel> feedbacks = feedbackRepository.findAllById(mapCoursesFeedbacks.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList())
        );

        //Mapping feedbacks where the key is the feeback ID and the value its corresponding object
        Map<Integer, FeedbackModel> feedbackMap = feedbacks.stream()
                .collect(Collectors.toMap(FeedbackModel::getId, feedback -> feedback));

        //Mapping courses where the key is the course ID and the value its calculated NPS
        Map<Integer, Float> mapCourseNPS = mapCoursesFeedbacks.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<Integer> feedbackIds = entry.getValue();
                            float promoters = feedbackIds.stream()
                                    .map(feedbackMap::get)
                                    .filter(feedback -> feedback != null && feedback.getRating() != null && feedback.getRating() >= 9)
                                    .count();

                            float detractors = feedbackIds.stream()
                                    .map(feedbackMap::get)
                                    .filter(feedback -> feedback != null && feedback.getRating() != null && feedback.getRating() < 6)
                                    .count();

                            float total = feedbackIds.size();

                            return calculateNPS(promoters, detractors, total);
                        }
                ));

        List<GetCoursesReportResponse> response = coursesMap.keySet().stream()
                .map(c -> new GetCoursesReportResponse(
                        c,
                        coursesMap.get(c).getName(),
                        mapCoursesRegistrationCount.get(c),
                        mapCoursesFeedbacks.get(c).size(),
                        mapCourseNPS.get(c),
                        coursesMap.get(c).getStatus()
                )).collect(Collectors.toList());

        return response;
    }

    private float calculateNPS(float promoters, float detractors, float total) {
        if (total == 0)
            return 0;

        return ((promoters - detractors) / total) * 100;
    }
}
