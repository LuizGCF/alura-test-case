package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostRegistrationRequest;
import com.alura.coursecase.exception.RequestValidationException;
import com.alura.coursecase.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("registration")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping
    public ResponseEntity register(@RequestBody @Validated PostRegistrationRequest postRegistrationRequest, BindingResult errors) {
        if (errors.hasErrors())
            throw new RequestValidationException(errors);

        registrationService.register(postRegistrationRequest.toRegistrationModel());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
