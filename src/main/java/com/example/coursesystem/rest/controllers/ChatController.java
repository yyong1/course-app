package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.model.Chat;
import com.example.coursesystem.core.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
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

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        Chat savedChat = chatService.save(chat);
        return new ResponseEntity<>(savedChat, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Chat> updateChat(@RequestBody Chat chat) {
        if (chat.getId() == null || !chatService.existsById(chat.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Chat updatedChat = chatService.save(chat);
        return new ResponseEntity<>(updatedChat, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable String id) {
        if (!chatService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        chatService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
