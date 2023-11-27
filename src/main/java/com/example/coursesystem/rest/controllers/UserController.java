package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.service.UserService;
import com.example.coursesystem.rest.dto.UserGetDTO;
import com.example.coursesystem.rest.dto.UserRequestDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "JWT Security")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserGetDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @RequestMapping(method = RequestMethod.POST, path = "/register")
//    public ResponseEntity<UserGetDTO> createUser(@RequestBody UserRequestDTO user) {
//        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGetDTO> updateUser(@PathVariable String id, @RequestBody UserRequestDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(id, userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
