package com.example.coursesystem.rest.configuration;

import com.example.coursesystem.rest.websockets.MainSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    private final MainSocketHandler mainSocketHandler;

    public WebSocketConfiguration(MainSocketHandler mainSocketHandler) {
        this.mainSocketHandler = mainSocketHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mainSocketHandler, "/websocket").setAllowedOrigins("*");
    }
}