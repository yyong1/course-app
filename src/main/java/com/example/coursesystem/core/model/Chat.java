package com.example.coursesystem.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "chats")
public class Chat {

    @Id
    private String id;
    private String chatName;
    private Set<String> userIds;
    private List<Message> messages;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    public Set<String> getUserIds() { return userIds; }

    public void setUserIds(Set<String> userIds) { this.userIds = userIds; }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
