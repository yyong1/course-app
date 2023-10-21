package com.example.coursesystem.core.repository;

import com.example.coursesystem.core.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    List<User> findByLastName(String lastName);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByUsername(String username);

    List<User> findByCoursesEnrolled(String courseId);

    List<User> findByRolesContains(String role);

}
