package com.example.coursesystem.core.model;

import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserDetailsImplementation() {
        String expectedUsername = "testUser";
        String expectedPassword = "password";
        String expectedEmail = "test@example.com";
        UserRole expectedRole = UserRole.GUEST;
        Date expectedCreationDate = new Date();

        user.setUsername(expectedUsername);
        user.setPassword(expectedPassword);
        user.setEmail(expectedEmail);
        user.setRole(expectedRole);
        user.setCreationDate(expectedCreationDate);

        assertThat(user.getUsername()).isEqualTo(expectedUsername);
        assertThat(user.getPassword()).isEqualTo(expectedPassword);
        assertThat(user.getEmail()).isEqualTo(expectedEmail);
        assertThat(user.getRole()).isEqualTo(expectedRole);
        assertThat(user.getCreationDate()).isEqualTo(expectedCreationDate);

        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertThat(authorities).isNotNull();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo(expectedRole.name());
    }
}
