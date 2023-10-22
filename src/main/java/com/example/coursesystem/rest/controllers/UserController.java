package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.service.UserService;
import com.example.coursesystem.rest.dto.UserGetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserGetDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserGetDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable String id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new ResponseEntity<>(new UserGetDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity<UserGetDTO> createUser(@RequestBody UserGetDTO userDTO) {
        User user = convertToEntity(userDTO);
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(new UserGetDTO(createdUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGetDTO> updateUser(@PathVariable String id, @RequestBody UserGetDTO userDTO) {
        User user = convertToEntity(userDTO);
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return new ResponseEntity<>(new UserGetDTO(updatedUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private User convertToEntity(UserGetDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setCreationDate(userDTO.getCreationDate());
        return user;
    }
}
