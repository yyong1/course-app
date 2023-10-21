package com.example.coursesystem.core.service;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Optional<Chat> findById(String id) {
        return chatRepository.findById(id);
    }

    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    public void deleteById(String id) {
        chatRepository.deleteById(id);
    }
    public boolean existsById(String id) {
        return chatRepository.existsById(id);
    }

    public Optional<Chat> findByUserId(String userId) {
        return chatRepository.findByUserId(userId);
    }
}