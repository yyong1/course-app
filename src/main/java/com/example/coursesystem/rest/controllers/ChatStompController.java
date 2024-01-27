package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.model.Message;
import com.example.coursesystem.core.service.ChatService;
import com.example.coursesystem.core.service.MessageService;
import com.example.coursesystem.core.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ChatStompController {
    private final ChatService chatService;
    private final MessageService messageService;
    private final NotificationService notificationService;

    @Autowired
    public ChatStompController(ChatService chatService, MessageService messageService, NotificationService notificationService) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.notificationService = notificationService;
    }

    @MessageMapping("/chat/create")
    public void createChat(Chat chat) {
        Chat newChat = chatService.createNewChat(chat);
        notificationService.notifyNewChat(newChat);
    }

    @MessageMapping("/chat/message")
    public void handleReceivedMessage(Message msg) {
        System.out.println("Received message: " + msg);
        Message savedMessage = messageService.saveChatMessage(msg.getChatId(), msg);
        Optional<Chat> chatOptional = chatService.findById(msg.getChatId()); // check
        chatOptional.ifPresent(chat -> {
            notificationService.notifyNewMessage(chat.getId(), savedMessage);
        });
    }
}
