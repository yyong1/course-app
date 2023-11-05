package com.example.coursesystem.core.service;

import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.repository.UserRepository;
import com.example.coursesystem.rest.dto.UserGetDTO;
import com.example.coursesystem.rest.dto.UserRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserGetDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserGetDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<UserGetDTO> getUserById(String id) {
        return userRepository.findById(id).map(UserGetDTO::new);
    }

    public UserGetDTO createUser(UserRequestDTO payload) {
        return new UserGetDTO(userRepository.save(payload.toEntity()));
    }

    public UserGetDTO updateUser(String id, UserRequestDTO userDTO) {
        if (userRepository.existsById(id)) {
            User updatedUser = userDTO.toEntity();
            updatedUser.setId(id);
            return new UserGetDTO(userRepository.save(updatedUser));
        }
        return null;
    }
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
