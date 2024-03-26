package com.alura.coursecase.utils.request;

import com.alura.coursecase.controllers.request.PostFeedbackRequest;
import com.alura.coursecase.utils.RegistrationCreator;

public class PostFeedbackRequestCreator {

    public static PostFeedbackRequest createPostFeedbackRequestWithGoodRatingToBeSaved(){
        return new PostFeedbackRequest("Feedback Title", "Feedback description", RegistrationCreator.createValidRegistrationForFeedbackSending().getId(), 10);
    }

    public static PostFeedbackRequest createInvalidPostFeedbackRequestWithBadRating(){
        return new PostFeedbackRequest("", "", RegistrationCreator.createValidRegistrationForFeedbackSending().getId(), 5);
    }

    public static PostFeedbackRequest createInvalidRegistrationPostFeedbackRequest(){
        return new PostFeedbackRequest("Feedback Title", "Feedback description", 99, 10);
    }

    public static PostFeedbackRequest createAlreadySentFeedbackPostFeedbackRequest(){
        return new PostFeedbackRequest("Feedback Title", "Feedback description", RegistrationCreator.createValidRegistrationWithFeedback().getId(), 10);
    }

    public static PostFeedbackRequest createInactiveCoursePostFeedbackRequest(){
        return new PostFeedbackRequest("Feedback Title", "Feedback description", RegistrationCreator.createInactiveCourseRegistration().getId(), 10);
    }

    public static PostFeedbackRequest createInvalidFieldsPostFeedbackRequest(){
        return new PostFeedbackRequest("", "", null, null);
    }
}
