package com.example.enrollmentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class ExceptionConfig {

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }//NotFoundException: Used when something is not found (like student or enrollment missing).
    }

    public static class ConflictException extends RuntimeException {
        public ConflictException(String message) {
            super(message);
        }//ConflictException: Used when there’s a conflict, like trying to enroll a student twice in the same course.
    }

    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }//BadRequestException: Used for invalid inputs (wrong data format, missing params, etc.).
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }//UnauthorizedException: Used when user is not allowed to perform an action
    }

    @RestControllerAdvice //Marks this as a global exception handler.
    public static class GlobalExceptionHandler {//Any exception thrown in your controllers/services will be caught here.

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<String> handleNotFound(NotFoundException ex) {//Returns 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<String> handleConflict(ConflictException ex) {//Returns 409 CONFLICT with the message.
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<String> handleBadRequest(BadRequestException ex) {//Returns 400 BAD REQUEST.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<String> handleUnauthorized(UnauthorizedException ex) {//Returns 401 UNAUTHORIZED.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGeneric(Exception ex) {//Returns 500 INTERNAL SERVER ERROR
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error: " + ex.getMessage());
        }
    }
}
