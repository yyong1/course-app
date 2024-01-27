package com.example.coursesystem.rest.controllers;

import com.example.coursesystem.core.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Add endpoints if you need to trigger notifications via HTTP requests
    // Typically, notification logic is directly triggered by other services or controllers
}
