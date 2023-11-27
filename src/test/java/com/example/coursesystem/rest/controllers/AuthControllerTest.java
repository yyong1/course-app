//package com.example.coursesystem.rest.controllers;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//import com.example.coursesystem.core.model.User;
//import com.example.coursesystem.core.service.AuthService;
//import com.example.coursesystem.rest.dto.LoginDTO;
//import com.example.coursesystem.rest.dto.LoginRequestDTO;
//import com.example.coursesystem.rest.dto.UserGetDTO;
//import com.example.coursesystem.rest.dto.UserRequestDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.http.MediaType;
//import com.example.coursesystem.core.model.enums.UserRole;
//
//import java.util.Date;
//

// TODO: Fix this test for AuthController

//@WebMvcTest(AuthController.class)
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @MockBean
//    private AuthService authService;
//
//    @InjectMocks
//    private AuthController authController;
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void testRegister() throws Exception {
//        UserRequestDTO userRequestDTO = new UserRequestDTO();
//        userRequestDTO.setUsername("johndoe");
//        userRequestDTO.setPassword("password123");
//        userRequestDTO.setEmail("johndoe@example.com");
//        userRequestDTO.setRole(UserRole.GUEST);
//        userRequestDTO.setCreationDate(new Date());
//
//        User testUser = new User();
//        testUser.setId("1");
//        testUser.setUsername("johndoe");
//        testUser.setEmail("johndoe@example.com");
//        testUser.setRole(UserRole.GUEST);
//        testUser.setCreationDate(new Date());
//        UserGetDTO userGetDTO = new UserGetDTO(testUser);
//
//        given(authService.signUp(any(UserRequestDTO.class))).willReturn(userGetDTO);
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRequestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(userGetDTO)));
//    }
//
//    @Test
//    void testLogin() throws Exception {
//        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("johndoe@example.com", "password123");
//        LoginDTO loginDTO = new LoginDTO("jwt-token");
//
//        given(authService.signIn(any(LoginRequestDTO.class))).willReturn(loginDTO);
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(loginDTO)));
//    }
//}
