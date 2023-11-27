package com.example.coursesystem.core.repository;

import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private User user;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(User.class);
        user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(UserRole.GUEST);
        user.setCreationDate(new Date());
        userRepository.save(user);
    }

    @Test
    void testFindAll() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void testFindById() {
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void testSave() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("new@example.com");
        newUser.setRole(UserRole.GUEST);
        newUser.setCreationDate(new Date());

        User savedUser = userRepository.save(newUser);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
    }

    @Test
    void testDeleteById() {
        userRepository.deleteById(user.getId());
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void testExistsById() {
        boolean exists = userRepository.existsById(user.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByUsernameOrEmail() {
        Optional<User> foundByEmail = userRepository.findByUsernameOrEmail(user.getEmail());
        assertThat(foundByEmail).isPresent();
        assertThat(foundByEmail.get().getEmail()).isEqualTo(user.getEmail());

        Optional<User> foundByUsername = userRepository.findByUsernameOrEmail(user.getUsername());
        assertThat(foundByUsername).isPresent();
        assertThat(foundByUsername.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void testFindByEmail() {
        User foundUser = userRepository.findByEmail(user.getEmail());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }
}
