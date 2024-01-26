package com.example.coursesystem.core.service;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.model.Message;
import com.example.coursesystem.core.repository.ChatRepository;
import com.example.coursesystem.core.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    public Message saveChatMessage(String chatId, Message message) {
        message.setChatId(chatId);
        Message savedMessage = messageRepository.save(message);

        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        chatOptional.ifPresent(chat -> {
            chat.getMessages().add(savedMessage);
            chatRepository.save(chat);
        });

        return savedMessage;
    }

    public List<Message> getMessagesByChatId(String chatId) {
        return messageRepository.findByChatId(chatId);
    }
}
