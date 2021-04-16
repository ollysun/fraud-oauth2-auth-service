package com.etz.authorisationserver.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    private DateTime dateofError;
    private String message;
    private HttpStatus status;
    private List<String> errors;

    public ExceptionResponse() {
        super();
    }

    public ExceptionResponse(DateTime dateofError, String message, HttpStatus status, List<String> errors) {
        super();
        this.dateofError = dateofError;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public ExceptionResponse(DateTime dateofError, String message, HttpStatus status) {
        this.dateofError = dateofError;
        this.message = message;
        this.status = status;
    }
}
