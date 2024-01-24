package com.example.coursesystem.core.service;

import com.example.coursesystem.core.exeptions.repository.ResourceNotFoundException;
import com.example.coursesystem.core.exeptions.user.UserAlreadyExistsException;
import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.repository.UserRepository;
import com.example.coursesystem.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

        private final UserRepository userRepository;
        private final UserService userService;

        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private JwtService jwtService;
        @Autowired
        private AuthenticationManager authenticationManager;
        public AuthService(UserRepository userRepository, UserService userService) {
            this.userRepository = userRepository;
            this.userService = userService;
        }

    public UserGetDTO signUp(UserRequestDTO userRequestDTO) {
        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        boolean exists = userRepository.existsByUsernameOrEmail(userRequestDTO.getUsername(), userRequestDTO.getEmail());
        if (exists) {
            throw new UserAlreadyExistsException("Username or email is already taken");
        }
        User user = userRepository.save(userRequestDTO.toEntity());
        return new UserGetDTO(user);
    }

    public LoginDTO signIn(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
        User user = userRepository.findByUsernameOrEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("This user does not exist."));
        String jwt = jwtService.generateToken(user);

        return new LoginDTO(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public JwtGetDTO refreshToken(String jwtRefresh) {
        if (jwtRefresh != null) {
            User user = userService.findUserByUsernameOrEmail(jwtService.extractUserName(jwtRefresh));
            if (user != null) {
                String jwtRefreshToken = jwtService.generateRefreshToken(user);
                return new JwtGetDTO(jwtRefreshToken);
            }
        }
        throw new BadCredentialsException("Invalid Credentials");
    }
}