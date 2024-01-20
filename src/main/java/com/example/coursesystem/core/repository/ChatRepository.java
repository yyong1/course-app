package com.example.coursesystem.core.repository;

import com.example.coursesystem.core.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

    List<Chat> findByUserIdsContains(String userId);
    Optional<Chat> findByChatName(String chatName);
    Optional<Chat> findById(String id);
    @Override
    Chat save(Chat chat);
    void deleteById(String id);
}
