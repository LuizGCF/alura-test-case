package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostUserRequest;
import com.alura.coursecase.controllers.response.GetUserResponse;
import com.alura.coursecase.exception.UserValidationException;
import com.alura.coursecase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Validated PostUserRequest postUserRequest, BindingResult errors) {
        if (errors.hasErrors())
            throw new UserValidationException(errors);

        userService.createUser(postUserRequest.toUserModel());
    }

    @GetMapping("/{username}")
    public GetUserResponse findUserByUsername(@PathVariable("username") String username) {
        return userService.findUserByUsername(username);
    }

}
