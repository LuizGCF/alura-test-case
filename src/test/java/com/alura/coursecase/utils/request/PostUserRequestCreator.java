package com.alura.coursecase.utils.request;

import com.alura.coursecase.controllers.request.PostUserRequest;
import com.alura.coursecase.enums.RolesEnum;

public class PostUserRequestCreator {

    public static PostUserRequest createPostUserRequestToBeSaved(){
        return new PostUserRequest("John", "john", "john@gmail.com", "password", RolesEnum.STUDENT);
    }

    public static PostUserRequest createInvalidPostUserRequest(){
        return new PostUserRequest("", "john1", "johngmailcom", "", null);
    }
}
