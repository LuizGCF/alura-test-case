package com.alura.coursecase.utils;

import com.alura.coursecase.models.FeedbackModel;

import java.time.LocalDateTime;

public class FeedbackCreator {

    public static FeedbackModel createFeedbackToBeSaved(){
        return FeedbackModel.builder()
                .title("Greate course")
                .description("Well presented course.")
                .rating(10)
                .feedbackDate(LocalDateTime.now())
                .idRegistration(1)
                .build();
    }

    public static FeedbackModel createFeedbackToBeSavedWithLowRating(){
        return FeedbackModel.builder()
                .title("Outdated course")
                .description("Old technologies used.")
                .rating(5)
                .feedbackDate(LocalDateTime.now())
                .idRegistration(1)
                .build();
    }

    public static FeedbackModel createValidFeedback(){
        return FeedbackModel.builder()
                .id(1)
                .title("Greate course")
                .description("Well presented course.")
                .rating(10)
                .feedbackDate(LocalDateTime.now())
                .idRegistration(1)
                .build();
    }

    public static FeedbackModel createValidFeedbackWithLowRating(){
        return FeedbackModel.builder()
                .id(1)
                .title("Outdated course")
                .description("Old technologies used.")
                .rating(5)
                .feedbackDate(LocalDateTime.now())
                .idRegistration(1)
                .build();
    }

    public static FeedbackModel createInvalidFeedback(){
        return FeedbackModel.builder()
                .title(null)
                .description("")
                .rating(5)
                .feedbackDate(LocalDateTime.now())
                .build();
    }


}
