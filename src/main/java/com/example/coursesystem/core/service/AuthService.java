package com.example.coursesystem.core.service;

import com.example.coursesystem.core.exeptions.repository.ResourceNotFoundException;
import com.example.coursesystem.core.exeptions.user.UserAlreadyExistsException;
import com.example.coursesystem.core.model.User;
import com.example.coursesystem.rest.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Value("${security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${website.frontend.url}")
    private String frontendUrl;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserGetDTO signUp(UserRequestDTO userRequestDTO) {
        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        boolean exists = userService.existsByUsernameOrEmail(userRequestDTO.getUsername(), userRequestDTO.getEmail());
        if (exists) {
            throw new UserAlreadyExistsException("Username or email is already taken");
        }
        User user = userService.save(userRequestDTO.toEntity());
        return new UserGetDTO(user);
    }

    public LoginDTO signIn(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
        User user = userService.findByUsernameOrEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("This user does not exist."));

        // extra claims
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());
        extraClaims.put("email", user.getEmail());

        String jwt = jwtService.generateToken(extraClaims, user);

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

    public UserTokenInfo authGoogle(String payload) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload);
        String code = jsonNode.get("payload").get("code").asText();

        GoogleTokenResponse tokenResponse = exchangeCodeForToken(code);
        if (tokenResponse.getAccessToken() == null) {
            throw new BadCredentialsException("Failed to retrieve access token");
        }

        GoogleUserInfo userInfo = getGoogleUserInfo(tokenResponse.getAccessToken());
        User user = userService.registerOrGetUserFromGoogle(userInfo, tokenResponse);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());
        extraClaims.put("email", user.getEmail());

        String jwt = jwtService.generateToken(extraClaims, user);

        return new UserTokenInfo(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }


    public GoogleTokenResponse exchangeCodeForToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://oauth2.googleapis.com/token";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);
        requestBody.put("client_id", googleClientId);
        requestBody.put("client_secret", googleClientSecret);
        requestBody.put("redirect_uri", frontendUrl);
        requestBody.put("grant_type", "authorization_code");

        try {
            ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(url, requestBody, GoogleTokenResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("Error during token exchange: Status Code: {}", response.getStatusCode());
                throw new HttpClientErrorException(response.getStatusCode(), "Error during token exchange.");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error during token exchange: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("System error during token exchange: {}", e.getMessage());
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public GoogleUserInfo getGoogleUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;

        ResponseEntity<GoogleUserInfo> response = restTemplate.getForEntity(url, GoogleUserInfo.class);
        return response.getBody();
    }
}