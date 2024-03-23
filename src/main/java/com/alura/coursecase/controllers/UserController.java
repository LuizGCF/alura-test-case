package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostUserRequest;
import com.alura.coursecase.controllers.response.GetUserResponse;
import com.alura.coursecase.exception.RequestValidationException;
import com.alura.coursecase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Validated PostUserRequest postUserRequest, BindingResult errors) {
        if (errors.hasErrors())
            throw new RequestValidationException(errors);

        userService.createUser(postUserRequest.toUserModel());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<GetUserResponse> findUserByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.findUserByUsername(username));
    }

}
