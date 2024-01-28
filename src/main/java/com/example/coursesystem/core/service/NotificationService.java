package com.example.coursesystem.core.service;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.model.Message;
import com.example.coursesystem.core.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate, ChatRepository chatRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }

    public void notifyNewChat(Chat chat) {
        String destination = "/topic/chat/" + chat.getId();
        messagingTemplate.convertAndSend(destination, chat);
        System.out.println("Sent chat notification to topic: " + destination);
    }

    public void notifyNewMessage(String chatId, Message message) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() ->
                new IllegalArgumentException("Chat not found with id: " + chatId)
        );

        String destination = "/topic/chat/" + chatId;
        messagingTemplate.convertAndSend(destination, message);
        System.out.println("Sent message notification to topic: " + destination);
    }
}
