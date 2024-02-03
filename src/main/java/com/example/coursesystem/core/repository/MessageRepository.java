package com.example.coursesystem.core.repository;

import com.example.coursesystem.core.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChatId(String chatId);

    @Override
    Message save(Message message);
}

