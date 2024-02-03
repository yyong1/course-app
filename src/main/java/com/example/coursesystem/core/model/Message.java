package com.example.coursesystem.core.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Document
public class Message {
    @Id
    private String id;
    private String senderId;
    private String chatId;
    private String content;
    private Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            this.timestamp = dateFormat.parse(timestamp);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is 'yyyy-MM-dd HH:mm:ss.SSS'", e);
        }
    }

}
