package com.etz.authorisationserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ExceptionResponse {

    private LocalDateTime timeStamp;
    private String message;
    private HttpStatus status;
    private List<String> errors;

    public ExceptionResponse() {
        super();
    }

    public ExceptionResponse(LocalDateTime dateofError, String message, HttpStatus status, List<String> errors) {
        super();
        this.timeStamp = dateofError;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public ExceptionResponse(LocalDateTime dateofError, String message, HttpStatus status) {
        this.timeStamp = dateofError;
        this.message = message;
        this.status = status;
    }
}
