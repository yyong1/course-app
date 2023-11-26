package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.service.AuthService;
import com.example.coursesystem.rest.dto.LoginDTO;
import com.example.coursesystem.rest.dto.LoginRequestDTO;
import com.example.coursesystem.rest.dto.UserGetDTO;
import com.example.coursesystem.rest.dto.UserRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public ResponseEntity<UserGetDTO> register(@RequestBody UserRequestDTO user) {
        return ResponseEntity.ok(authService.signUp(user));
    }


    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<LoginDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.signIn(loginRequest));
    }
}

