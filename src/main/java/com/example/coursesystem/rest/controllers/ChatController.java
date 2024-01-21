package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.coursesystem.rest.websockets.MainSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final MainSocketHandler mainSocketHandler;

    public ChatController(ChatService chatService, MainSocketHandler mainSocketHandler) {
        this.chatService = chatService;
        this.mainSocketHandler = mainSocketHandler;
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getAllChats() {
        return new ResponseEntity<>(chatService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable String id) {
        Optional<Chat> chatOptional = chatService.findById(id);
        if (chatOptional.isPresent()) {
            return new ResponseEntity<>(chatOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getChatsByUserId(@PathVariable String userId) {
        List<Chat> userChats = chatService.findByUserId(userId);
        if (!userChats.isEmpty()) {
            return new ResponseEntity<>(userChats, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        Chat savedChat = chatService.createNewChat(chat);
//        savedChat.getUserIds().forEach(memberId -> {
//            try {
//                mainSocketHandler.sendNewChatNotificationToUser(memberId, savedChat);
//            } catch ( IOException e) {
//                e.printStackTrace();
//            }
//        });
        return new ResponseEntity<>(savedChat, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<Chat> updateChatName(@PathVariable String id, @RequestBody String newChatName) {
        Optional<Chat> updatedChat = chatService.editChatName(id, newChatName);
        return updatedChat.map(chat -> new ResponseEntity<>(chat, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/members")
    public ResponseEntity<Chat> updateChatMembers(@PathVariable String id, @RequestBody Set<String> newMembers) {
        Optional<Chat> updatedChat = chatService.editChatMembers(id, newMembers);
        return updatedChat.map(chat -> new ResponseEntity<>(chat, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable String id) {
        if (chatService.existsById(id)) {
            chatService.deleteChat(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}