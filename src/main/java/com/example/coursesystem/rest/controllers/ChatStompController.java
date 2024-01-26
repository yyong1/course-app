package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.model.Message;
import com.example.coursesystem.core.service.ChatService;
import com.example.coursesystem.core.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ChatStompController {
    private final ChatService chatService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatStompController(ChatService chatService, MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/create")
    public void createChat(Chat chat) {
        Chat newChat = chatService.createNewChat(chat);
        newChat.getUserIds().forEach(userId ->
                messagingTemplate.convertAndSendToUser(userId, "/queue/chat", newChat)
        );
    }

    @MessageMapping("/chat/message")
    public void handleReceivedMessage(Message msg) {
        System.out.println("Received message: " + msg);
        System.out.println("Received time: " + msg.getTimestamp());
        Message savedMessage = messageService.saveChatMessage(msg.getChatId(), msg);
        Optional<Chat> chatOptional = chatService.findById(msg.getChatId());
        System.out.println("Received chatOptional: " + chatOptional);
        chatOptional.ifPresentOrElse(
                chat -> {
                    chat.getUserIds().forEach(userId ->
                            messagingTemplate.convertAndSendToUser(userId, "/queue/message", savedMessage)
                    );
                },
                () -> {
                    System.out.println("Chat not found for id: " + msg.getChatId());
                }
        );
    }

}