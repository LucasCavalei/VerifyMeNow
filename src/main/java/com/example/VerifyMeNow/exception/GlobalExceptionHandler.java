package com.example.VerifyMeNow.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<StandardError> handleUserRegistrationException(UserRegistrationException ex) {
        log.error("User registration exception: {}", ex.getMessage(), ex);
        StandardError standardError = new StandardError(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage());
        return ResponseEntity.badRequest().body(standardError);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<StandardError> handleUserLoginException(UserLoginException ex) {
        log.error("User login exception: {}", ex.getMessage(), ex);
        StandardError standardError = new StandardError(HttpStatus.UNAUTHORIZED.value(), new Date(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(standardError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleOtherExceptions(Exception ex) {
        log.error("User handle  **other  exception: {}", ex.getMessage(), ex);
        StandardError standardError = new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),  new Date(),"Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(standardError);
    }
}