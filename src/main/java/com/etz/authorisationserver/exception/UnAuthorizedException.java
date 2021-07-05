package com.etz.authorisationserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnAuthorizedException extends AuthenticationException  {
    public UnAuthorizedException(String message) {
        super(message);
    }
}