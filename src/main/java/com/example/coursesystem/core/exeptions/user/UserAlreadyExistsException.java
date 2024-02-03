package com.example.coursesystem.core.exeptions.user;

import com.example.coursesystem.core.exeptions.GeneralException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends GeneralException {
    public UserAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT.value(), message);
    }
}
