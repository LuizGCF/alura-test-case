package com.alura.coursecase.exception;

import com.alura.coursecase.controllers.response.ExceptionResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({RequestValidationException.class})
    public ResponseEntity<Object> handleRequestValidationException(RequestValidationException exception) {
        ExceptionResponse response = new ExceptionResponse(ErrorsEnum.A002.code, ErrorsEnum.A002.message, HttpStatus.UNPROCESSABLE_ENTITY.value(), LocalDateTime.now(),
                exception.getErrors().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({TokenGenerationFailedException.class})
    public ResponseEntity<Object> handleTokenGenerationFailedException(TokenGenerationFailedException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<Object> handleTokenInvalidTokenException(InvalidTokenException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({CourseCodeAlreadyExistsException.class})
    public ResponseEntity<Object> handleCourseCodeAlreadyExistsException(CourseCodeAlreadyExistsException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({InvalidRoleException.class})
    public ResponseEntity<Object> handleInvalidRoleException(InvalidRoleException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({CourseNotFoundException.class})
    public ResponseEntity<Object> handleCourseNotFoundException(CourseNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({CourseAlreadyInactiveException.class})
    public ResponseEntity<Object> handleCourseAlreadyInactiveException(CourseAlreadyInactiveException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        ExceptionResponse response = new ExceptionResponse(ErrorsEnum.A002.code, ErrorsEnum.A002.message, HttpStatus.UNPROCESSABLE_ENTITY.value(), LocalDateTime.now(),
                List.of(exception.getMessage()));
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ExceptionResponse response = new ExceptionResponse(ErrorsEnum.A002.code, ErrorsEnum.A002.message, HttpStatus.UNPROCESSABLE_ENTITY.value(), LocalDateTime.now(),
                List.of(exception.getMessage()));
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({UserAlreadyRegisteredException.class})
    public ResponseEntity<Object> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({RegistrationNotFoundException.class})
    public ResponseEntity<Object> handleCRegistrationNotFoundException(RegistrationNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({FeedbackAlreadySentException.class})
    public ResponseEntity<Object> handleFeedbackAlreadySentException(FeedbackAlreadySentException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({InvalidFeedbackObjectException.class})
    public ResponseEntity<Object> handleInvalidFeedbackObjectException(InvalidFeedbackObjectException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({FeedbackNotFoundException.class})
    public ResponseEntity<Object> handleFeedbackNotFoundException(FeedbackNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getCode(), exception.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), null);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
