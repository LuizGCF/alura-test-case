package com.alura.coursecase.exception;

import com.alura.coursecase.controllers.response.ExceptionResponse;
import com.alura.coursecase.enums.ErrorsEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
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

    @ExceptionHandler({UserValidationException.class})
    public ResponseEntity<Object> handleUserValidationException(UserValidationException exception) {
        ExceptionResponse response = new ExceptionResponse(ErrorsEnum.A103.code, ErrorsEnum.A103.message, HttpStatus.UNPROCESSABLE_ENTITY.value(), LocalDateTime.now(),
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
}
