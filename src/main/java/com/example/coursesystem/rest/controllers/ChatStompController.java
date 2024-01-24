package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatStompController {
    private final ChatService chatService;
    public ChatStompController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/create")
    @SendTo("/topic/newChat")
    public Chat createChat(Chat chat) {
        return chatService.createNewChat(chat);
    }
}