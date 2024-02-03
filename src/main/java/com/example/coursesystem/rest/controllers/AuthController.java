package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.service.AuthService;
import com.example.coursesystem.rest.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
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

    @RequestMapping(method = RequestMethod.POST, path = "/refresh")
    public ResponseEntity<JwtGetDTO> refreshToken(@RequestBody String jwtGetDTO) {
        return ResponseEntity.ok(authService.refreshToken(jwtGetDTO));
    }

    @PostMapping("/oauth2/google")
    public ResponseEntity<UserTokenInfo> loginOauth2Google(@RequestBody String tokenResponse) {
        try {
            return ResponseEntity.ok(authService.authGoogle(tokenResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

