package com.etz.authorisationserver.exception;

public class AuthServiceException extends RuntimeException{
    public AuthServiceException(String message) {
        super(message);
    }
}
