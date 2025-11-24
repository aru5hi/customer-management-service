package com.example.restApi.Exceptions;

import com.example.restApi.DTO.ErrorResponseModel;
import com.example.restApi.DTO.FieldErrorsModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles IllegalArgumentException - thrown when method receives invalid arguments
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseModel> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                exception.getMessage(),
                "/api"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles MethodArgumentTypeMismatchException - thrown when method parameter type doesn't match
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseModel> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String message = String.format("Parameter '%s' should be of type '%s' but received value '%s'",
                exception.getName(),
                exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "unknown",
                exception.getValue());
        
        ErrorResponseModel errorResponse = new ErrorResponseModel(
                HttpStatus.BAD_REQUEST.value(),
                "Type Mismatch",
                message,
                "/api"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles BadCredentialsException - thrown when authentication fails
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseModel> handleBadCredentials(BadCredentialsException exception) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication Failed",
                "Invalid username or password",
                "/api/auth"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Handles ResourceNotFoundException - thrown when requested resource is not found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleResourceNotFound(ResourceNotFoundException exception) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                exception.getMessage(),
                "/api"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles ConstraintViolationException - thrown when validation constraints are violated
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseModel> handleConstraintViolation(ConstraintViolationException exception) {
        List<FieldErrorsModel> fieldErrors = new ArrayList<>();
        
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String rejectedValue = violation.getInvalidValue() != null ? violation.getInvalidValue().toString() : "null";
            String errorMessage = violation.getMessage();
            
            fieldErrors.add(new FieldErrorsModel(fieldName, rejectedValue, errorMessage));
        }
        
        ErrorResponseModel errorResponse = new ErrorResponseModel(
                HttpStatus.BAD_REQUEST.value(),
                "Constraint Violation",
                "Validation failed for one or more fields",
                "/api"
        );
        errorResponse.setErrors(fieldErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles MethodArgumentNotValidException - thrown when @Valid validation fails
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseModel> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<FieldErrorsModel> fieldErrors = new ArrayList<>();
        
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String rejectedValue = error.getRejectedValue() != null ? error.getRejectedValue().toString() : "null";
            String errorMessage = error.getDefaultMessage();
            
            fieldErrors.add(new FieldErrorsModel(fieldName, rejectedValue, errorMessage));
        });
        
        ErrorResponseModel errorResponse = new ErrorResponseModel(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Request validation failed for one or more fields",
                "/api"
        );
        errorResponse.setErrors(fieldErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles generic exceptions - catch-all for unhandled exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseModel> handleGenericException(Exception exception) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred while processing your request",
                "/api"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
