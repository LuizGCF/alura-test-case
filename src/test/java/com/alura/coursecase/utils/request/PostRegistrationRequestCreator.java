package com.alura.coursecase.utils.request;

import com.alura.coursecase.controllers.request.PostRegistrationRequest;
import com.alura.coursecase.utils.CourseCreator;
import com.alura.coursecase.utils.UserCreator;

public class PostRegistrationRequestCreator {

    public static PostRegistrationRequest createPostRegistrationRequestToBeSaved(){
        return new PostRegistrationRequest(UserCreator.createValidUser().getId(), CourseCreator.createValidCourse().getId());
    }

    public static PostRegistrationRequest createInvalidUserPostRegistrationRequest(){
        return new PostRegistrationRequest(99, CourseCreator.createValidCourse().getId());
    }

    public static PostRegistrationRequest createInvalidCoursePostRegistrationRequest(){
        return new PostRegistrationRequest(UserCreator.createValidUser().getId(), 99);
    }

    public static PostRegistrationRequest createInactiveCoursePostRegistrationRequest(){
        return new PostRegistrationRequest(UserCreator.createValidUser().getId(), CourseCreator.createInactiveCourse().getId());
    }

    public static PostRegistrationRequest createInvalidFieldsPostRegistrationRequest(){
        return new PostRegistrationRequest(null, null);
    }
}
