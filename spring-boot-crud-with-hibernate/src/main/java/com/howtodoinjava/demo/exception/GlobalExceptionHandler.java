package com.howtodoinjava.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setDetail(ex.getMessage());
    problemDetail.setProperty("timestamp", LocalDateTime.now());
    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setTitle("Validation Errors");

    Map<String, List<String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.groupingBy(
            FieldError::getField,
            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
        ));

    problemDetail.setDetail("Validation failed for one or more fields.");
    problemDetail.setProperty("fieldErrors", fieldErrors);
    problemDetail.setProperty("timestamp", LocalDateTime.now());
    return problemDetail;
  }

  // Handle all other uncaught exceptions
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGlobalException(Exception ex) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setTitle("Internal Server Error");
    problemDetail.setDetail("An unexpected error occurred.");
    problemDetail.setProperty("timestamp", LocalDateTime.now());
    return problemDetail;
  }
}