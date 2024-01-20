package com.example.coursesystem.core.service;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.model.Message;
import com.example.coursesystem.core.repository.ChatRepository;
import com.example.coursesystem.core.repository.MessageRepository;
import com.example.coursesystem.rest.websockets.MainSocketHandler;
import com.example.coursesystem.rest.websockets.WebSocketMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final WebSocketMessageHandler messageHandler;
    private MainSocketHandler mainSocketHandler;


    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, WebSocketMessageHandler messageHandler ) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.messageHandler = messageHandler;
        this.messageHandler.setMessageConsumer(this::handleReceivedMessage);
    }

    @Autowired // method injection avoid the circular dependency
    public void setMainSocketHandler(MainSocketHandler mainSocketHandler) {
        this.mainSocketHandler = mainSocketHandler;
    }

    public Chat createNewChat(Chat chat) { return chatRepository.save(chat); }

    public Optional<Chat> editChatName(String chatId, String newChatName) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        chat.ifPresent(c -> {
            c.setChatName(newChatName);
            chatRepository.save(c);
        });
        return chat;
    }

    public Optional<Chat> editChatMembers(String chatId, Set<String> newMembers) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        chat.ifPresent(c -> {
            c.setUserIds(newMembers);
            chatRepository.save(c);
        });
        return chat;
    }

    public void deleteChat(String id) {
        chatRepository.deleteById(id);
    }

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Optional<Chat> findById(String id) {
        return chatRepository.findById(id);
    }

    public boolean existsById(String id) {
        return chatRepository.existsById(id);
    }

    public List<Chat> findByUserId(String userId) {
        return chatRepository.findByUserIdsContains(userId);
    }


    public void handleReceivedMessage(Message message) {
        // Save the message to the database
        Message savedMessage = messageRepository.save(message);

        // Broadcast the message to chat participants
        // Assuming the message contains the chatId and you have a method to find participant IDs by chatId
        Set<String> participantIds = findParticipantIdsByChatId(savedMessage.getChatId());
        participantIds.forEach(participantId -> {
            try {
                mainSocketHandler.sendMessageToUser(participantId, savedMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Method to find participant IDs by chatId (implement as per your logic)
    private Set<String> findParticipantIdsByChatId(String chatId) {
        // Implement logic to find participant IDs based on chatId
        return new HashSet<>();
    }

}