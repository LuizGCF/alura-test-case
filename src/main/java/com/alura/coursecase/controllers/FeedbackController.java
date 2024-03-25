package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostFeedbackRequest;
import com.alura.coursecase.controllers.response.GetCoursesReportResponse;
import com.alura.coursecase.exception.RequestValidationException;
import com.alura.coursecase.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity sendFeedback(@RequestBody @Validated PostFeedbackRequest postFeedbackRequest, BindingResult errors) {
        if (errors.hasErrors())
            throw new RequestValidationException(errors);

        feedbackService.sendFeedback(postFeedbackRequest.toFeedbackModel());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/report")
    public ResponseEntity<List<GetCoursesReportResponse>> listCoursesReport(){
        List<GetCoursesReportResponse> response = feedbackService.listCoursesReport();
        return ResponseEntity.ok().body(response);
    }
}
