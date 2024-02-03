package com.example.coursesystem.rest.websockets;
import com.example.coursesystem.core.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Component
public class WebSocketMessageHandler {
    private Consumer<Message> messageConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setMessageConsumer(Consumer<Message> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public void handleMessage(String messagePayload) {
        try {
            Message message = objectMapper.readValue(messagePayload, Message.class);
            if (messageConsumer != null) {
                messageConsumer.accept(message);
            }
        } catch (IOException e) {
            System.out.println("Error parsing message payload");
        }
    }
}