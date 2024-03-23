package com.alura.coursecase.controllers;

import com.alura.coursecase.controllers.request.PostCourseRequest;
import com.alura.coursecase.controllers.response.GetCourseResponse;
import com.alura.coursecase.enums.StatusEnum;
import com.alura.coursecase.exception.RequestValidationException;
import com.alura.coursecase.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity createCourse(@RequestBody @Validated PostCourseRequest postCourseRequest, BindingResult errors) {
        if (errors.hasErrors())
            throw new RequestValidationException(errors);

        courseService.createCourse(postCourseRequest.toCourseModel());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<GetCourseResponse>> listCourses(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(required = false) StatusEnum status){
        return ResponseEntity.ok().body(courseService.listCourses(page, size, status)) ;
    }

    @PatchMapping("/deactivate/{code}")
    public ResponseEntity deactivateCourse(@PathVariable("code") String code){
        courseService.deactivateCourse(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
