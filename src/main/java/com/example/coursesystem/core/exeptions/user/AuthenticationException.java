package com.example.coursesystem.core.exeptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.example.coursesystem.core.exeptions.GeneralException;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends GeneralException {
    public AuthenticationException() {
        super(HttpStatus.UNAUTHORIZED.value());
    }

    public AuthenticationException(String message) {
        super(HttpStatus.UNAUTHORIZED.value(), message);
    }

    public AuthenticationException(String message, Exception e) {
        super(message, e);
    }

}
