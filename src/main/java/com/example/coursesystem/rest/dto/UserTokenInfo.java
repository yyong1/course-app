package com.example.coursesystem.rest.dto;

import com.example.coursesystem.core.model.enums.UserRole;

public class UserTokenInfo {
    private String jwtToken;
    private String userId;
    private String username;
    private String email;
    private UserRole role;

    public UserTokenInfo(String jwtToken, String userId, String username, String email, UserRole role) {
        this.jwtToken = jwtToken;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}
