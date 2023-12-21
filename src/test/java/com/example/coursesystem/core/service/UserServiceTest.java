package com.example.coursesystem.core.service;

import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.model.enums.UserRole;
import com.example.coursesystem.core.repository.UserRepository;
import com.example.coursesystem.rest.dto.UserGetDTO;
import com.example.coursesystem.rest.dto.UserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.GUEST);
        user.setCreationDate(new Date());
    }

    @Test
    void getAllUsersTest() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        List<UserGetDTO> result = userService.getAllUsers();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        Optional<UserGetDTO> result = userService.getUserById("1");
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void createUserTest() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserGetDTO result = userService.createUser(userRequestDTO);
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void updateUserTest() {
        when(userRepository.existsById("1")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserRequestDTO userDTO = new UserRequestDTO();
        UserGetDTO result = userService.updateUser("1", userDTO);
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void deleteUserTest() {
        doNothing().when(userRepository).deleteById("1");
        userService.deleteUser("1");
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void userDetailsServiceNotFoundTest() {
        when(userRepository.findByUsernameOrEmail("unknown")).thenReturn(Optional.empty());
        Throwable thrown = catchThrowable(() -> userService.userDetailsService().loadUserByUsername("unknown"));
        assertThat(thrown).isInstanceOf(UsernameNotFoundException.class);
    }
}
