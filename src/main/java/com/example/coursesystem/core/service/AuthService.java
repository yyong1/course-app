package com.example.coursesystem.core.service;

import com.example.coursesystem.core.exeptions.repository.ResourceNotFoundException;
import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.repository.UserRepository;
import com.example.coursesystem.rest.dto.LoginDTO;
import com.example.coursesystem.rest.dto.LoginRequestDTO;
import com.example.coursesystem.rest.dto.UserGetDTO;
import com.example.coursesystem.rest.dto.UserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

        private final UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private JwtService jwtService;
        @Autowired
        private AuthenticationManager authenticationManager;
        public AuthService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public UserGetDTO signUp(UserRequestDTO userRequestDTO) {
            userRequestDTO.setPassword(
                    passwordEncoder.encode(userRequestDTO.getPassword())
            );
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

            return new LoginDTO(jwt);
        }
}