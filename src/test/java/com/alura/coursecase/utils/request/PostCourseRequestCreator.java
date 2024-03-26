package com.alura.coursecase.utils.request;

import com.alura.coursecase.controllers.request.PostCourseRequest;
import com.alura.coursecase.utils.UserCreator;

public class PostCourseRequestCreator {

    public static PostCourseRequest createPostCourseRequestToBeSaved(){
        return new PostCourseRequest("Java Course", "spring", UserCreator.createValidInstructorUser().getId(),"Java course using spring boot");
    }

    public static PostCourseRequest createInvalidFieldsPostCourseRequest(){
        return new PostCourseRequest("", "spr1ng", null,"");
    }

    public static PostCourseRequest createInvalidUserPostCourseRequest(){
        return new PostCourseRequest("Java Course", "java", 99,"Java course using spring boot");
    }

    public static PostCourseRequest createInvalidAuthorPostCourseRequest(){
        return new PostCourseRequest("Java Course", "java", UserCreator.createValidUser().getId(),"Java course using spring boot");
    }
}
