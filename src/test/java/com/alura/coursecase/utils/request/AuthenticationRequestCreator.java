package com.alura.coursecase.utils.request;

import com.alura.coursecase.controllers.request.AuthenticationRequest;
import com.alura.coursecase.models.UserModel;
import com.alura.coursecase.utils.UserCreator;

public class AuthenticationRequestCreator {

    public static AuthenticationRequest createValidAuthenticationRequest() {
        UserModel user = UserCreator.createValidUserWithPasswordDecoded();
        return new AuthenticationRequest(user.getUsername(), user.getPassword());
    }

    public static AuthenticationRequest createInvalidAuthenticationRequest() {
        return new AuthenticationRequest("invalid", "invalid");
    }
}
