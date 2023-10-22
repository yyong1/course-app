package com.example.coursesystem.rest.dto;

import com.example.coursesystem.core.model.User;
import com.example.coursesystem.core.model.enums.UserRole;

import java.util.Date;

public class UserRequestDTO {
    private String username;

    private String password;

    private String email;

    private UserRole role;

    private Date creationDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public  UserRequestDTO() { }

    public UserRequestDTO(User user) {
        this.role = user.getRole();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public User toEntity() {
        User user = new User();
        user.setRole(role);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setCreationDate(new Date());
        return user;
    }
}
