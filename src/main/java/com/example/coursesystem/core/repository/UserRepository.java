package com.example.coursesystem.core.repository;

import com.example.coursesystem.core.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAll();

    Optional<User> findById(String id);

    User save(User user);

    void deleteById(String id);

    boolean existsById(String id);
}
