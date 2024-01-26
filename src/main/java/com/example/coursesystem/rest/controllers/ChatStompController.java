package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.model.Message;
import com.example.coursesystem.core.service.ChatService;
import com.example.coursesystem.core.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
        // Отправка сообщения пользователям, которые добавлены в чат
        newChat.getUserIds().forEach(userId ->
                messagingTemplate.convertAndSendToUser(userId, "/queue/chat", newChat)
        );
    }

    @MessageMapping("/chat/message")
    public void handleReceivedMessage(Message msg) {
        Message savedMessage = messageService.saveChatMessage(msg.getChatId(), msg);
        Optional<Chat> chatOptional = chatService.findById(msg.getChatId());

        chatOptional.ifPresent(chat -> {
            chat.getUserIds().forEach(userId ->
                    messagingTemplate.convertAndSendToUser(userId, "/queue/message", savedMessage)
            );
        });
    }


//
//    @MessageMapping("/chat/create")
//    @SendTo("/topic/newChat")
//    public Chat createChat(Chat chat) {
//        return chatService.createNewChat(chat);
//    }
//
//    @MessageMapping("/chat/message")
//    @SendTo("/topic/chat")
//    public Message handleReceivedMessage(String chatId, Message msg) {
//        return messageService.saveChatMessage(chatId, msg);
//    }
}