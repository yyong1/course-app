//package com.example.coursesystem.rest.controllers;
//
//import com.example.coursesystem.core.model.User;
//import com.example.coursesystem.core.service.AuthService;
//import com.example.coursesystem.core.service.JwtService;
//import com.example.coursesystem.core.service.UserService;
//import com.example.coursesystem.rest.configuration.SecurityConfiguration;
//import com.example.coursesystem.rest.dto.LoginDTO;
//import com.example.coursesystem.rest.dto.LoginRequestDTO;
//import com.example.coursesystem.rest.dto.UserGetDTO;
//import com.example.coursesystem.rest.dto.UserRequestDTO;
//import com.example.coursesystem.core.model.enums.UserRole;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.Date;
//import java.util.Optional;
//
//import static org.mockito.Mockito.when;
//
//

// TODO: Fix this test for UserController

//@WebMvcTest(AuthController.class)
//@AutoConfigureMockMvc
//@Import(SecurityConfiguration.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    JwtService jwtService;
//
//    @MockBean
//    private AuthService authService;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    AuthenticationProvider authenticationProvider;
//
//    @Test
//    public void testGetAllUsers() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testGetUserById() throws Exception {
//        String userId = "1";
//        UserGetDTO userDTO = new UserGetDTO(createDummyUser());
//        when(userService.getUserById(userId)).thenReturn(Optional.of(userDTO));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testUpdateUser() throws Exception {
//        String userId = "1";
//        UserRequestDTO userRequestDTO = new UserRequestDTO();
//        UserGetDTO updatedUser = new UserGetDTO(createDummyUser());
//        when(userService.updateUser(userId, userRequestDTO)).thenReturn(updatedUser);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userJson = objectMapper.writeValueAsString(userRequestDTO);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userJson))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testDeleteUser() throws Exception {
//        String userId = "1";
//        Mockito.doNothing().when(userService).deleteUser(userId);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//    }
//
//    private User createDummyUser() {
//        User user = new User();
//        user.setId("1");
//        user.setUsername("testUser");
//        user.setPassword("password");
//        user.setEmail("test@example.com");
//        user.setRole(UserRole.GUEST);
//        user.setCreationDate(new Date());
//        return user;
//    }
//}