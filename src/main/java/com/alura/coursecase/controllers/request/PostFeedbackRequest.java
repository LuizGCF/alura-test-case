package com.alura.coursecase.controllers.request;

import com.alura.coursecase.models.FeedbackModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostFeedbackRequest {


    private String title;

    private String description;

    @NotNull(message = "The attribute id_registration is required.")
    @Range(min = 1)
    private Integer idRegistration;

    @NotNull(message = "The attribute rating is required.")
    @Range(min = 1, max = 10, message = "The rating must be an integer between 1 and 10.")
    private Integer rating;

    public FeedbackModel toFeedbackModel() {
        return FeedbackModel.builder()
                .title(title)
                .description(description)
                .rating(rating)
                .feedbackDate(LocalDateTime.now())
                .idRegistration(idRegistration)
                .build();
    }
}
