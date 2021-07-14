package com.etz.authorisationserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler  {

    private final LocalDateTime todayDate = LocalDateTime.now();

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                todayDate,ex.getMessage(), HttpStatus.NOT_FOUND, details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleException(MethodArgumentNotValidException ex) {

        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                todayDate, "Parameters Not Valid",HttpStatus.BAD_REQUEST,
                errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleException(DataIntegrityViolationException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                todayDate, "Data Integrity Violation or Duplicate Entry",HttpStatus.CONFLICT,details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleException(DuplicateKeyException ex){
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                todayDate, "Similar Record already exist",HttpStatus.CONFLICT,null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Object> handleException(UnAuthorizedException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                todayDate, ex.getMessage(), HttpStatus.UNAUTHORIZED,
                 details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                todayDate, "Something went wrong while trying to process your request",
                HttpStatus.INTERNAL_SERVER_ERROR,
                details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<Object> handleException(AuthServiceException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                 todayDate, ex.getMessage(),HttpStatus.BAD_REQUEST,
                details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                todayDate, error,HttpStatus.NOT_FOUND,
                null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }


}
