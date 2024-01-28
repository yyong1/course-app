package com.example.coursesystem.core.service;

import com.example.coursesystem.core.exeptions.repository.ResourceNotFoundException;
import com.example.coursesystem.core.exeptions.user.UserAlreadyExistsException;
import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.repository.UserRepository;
import com.example.coursesystem.rest.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Value("${security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${website.frontend.url}")
    private String frontendUrl;

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
        // TODO: redo this for service
        boolean exists = userRepository.existsByUsernameOrEmail(userRequestDTO.getUsername(), userRequestDTO.getEmail());
        if (exists) {
            throw new UserAlreadyExistsException("Username or email is already taken");
        }
        // TODO: redo this for service
        User user = userRepository.save(userRequestDTO.toEntity());
        return new UserGetDTO(user);
    }

    public LoginDTO signIn(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
        User user = userRepository.findByUsernameOrEmail(loginRequestDTO.getEmail())
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
        System.out.println("GoogleTokenResponse tokenResponse ---> " + tokenResponse);
        GoogleUserInfo userInfo = getGoogleUserInfo(tokenResponse.getAccessToken());
        System.out.println("GoogleUserInfo userInfo ---> " + userInfo);

        Optional<User> existingUser = userRepository.findByUsernameOrEmail(userInfo.getEmail());
        User user;
        Map<String, Object> extraClaims = new HashMap<>();

        if (existingUser.isPresent()) {
            // login
            user = existingUser.get();
            extraClaims.put("id", user.getId());
            extraClaims.put("email", user.getEmail());

            String jwt = jwtService.generateToken(extraClaims, user);
            return new UserTokenInfo(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        } else {
            // register
            UserRequestDTO userRequestDTO = convertGoogleUserInfoToUserRequestDTO(userInfo);
            UserGetDTO userGetDTO = signUp(userRequestDTO);
            User newUser = userRepository.findByEmail(userInfo.getEmail());

            String jwt = jwtService.generateToken(extraClaims, newUser);
            return new UserTokenInfo(jwt, newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole());
        }
    }
    private UserRequestDTO convertGoogleUserInfoToUserRequestDTO(GoogleUserInfo googleUserInfo) {
        String username = userService.generateUniqueUsernameForGoogleAccount(googleUserInfo.getName(), googleUserInfo.getFamilyName());

        String randomPassword = userService.generateRandomPassword();

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail(googleUserInfo.getEmail());
        userRequestDTO.setUsername(googleUserInfo.getName());
        userRequestDTO.setPassword(randomPassword);

        return userRequestDTO;
    }
    public GoogleTokenResponse exchangeCodeForToken(String code) {
            System.out.println("code ---> " + code);

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://oauth2.googleapis.com/token";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);
        requestBody.put("client_id", googleClientId);
        requestBody.put("client_secret", googleClientSecret);
        requestBody.put("redirect_uri", frontendUrl);
        requestBody.put("grant_type", "authorization_code");
        System.out.println("requestBody ---> " + requestBody);

        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(url, requestBody, GoogleTokenResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Token Response ---> " + response.getBody());
            return response.getBody();
        } else {
            System.out.println("Error during token exchange: " + response.getStatusCode());
            return null;
        }
    }

    public GoogleUserInfo getGoogleUserInfo(String accessToken) {
            System.out.println("accessToken ---> " + accessToken);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;

        ResponseEntity<GoogleUserInfo> response = restTemplate.getForEntity(url, GoogleUserInfo.class);
        return response.getBody();
    }
}