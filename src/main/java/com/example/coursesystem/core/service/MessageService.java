package com.example.coursesystem.core.service;

import com.example.coursesystem.core.model.Message;
import com.example.coursesystem.core.repository.MessageRepository;
import com.example.coursesystem.core.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
//    private final ChatService chatService;
//    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageService(MessageRepository messageRepository
//                          ChatService chatService,
//                          SimpMessagingTemplate messagingTemplate
    ) {
        this.messageRepository = messageRepository;
//        this.chatService = chatService;
//        this.messagingTemplate = messagingTemplate;
    }

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatId(String chatId) {
        return messageRepository.findByChatId(chatId);
    }
}
