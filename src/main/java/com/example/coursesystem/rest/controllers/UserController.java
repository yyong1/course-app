package com.example.coursesystem.rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user")
    public String sayUser(@RequestParam(value = "name", defaultValue = "Andrey") String name) {
        return "Hello, " + name + "!";
    }
}
