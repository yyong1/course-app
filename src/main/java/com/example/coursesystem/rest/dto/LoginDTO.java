package com.example.coursesystem.rest.dto;

import com.example.coursesystem.core.model.enums.UserRole;

public class LoginDTO {
    private String jwt;
    private String username;
    private String email;
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
    public LoginDTO(String jwt, String username, String email, UserRole role) {
        this.jwt = jwt;
        this.username = username;
        this.email = email;
        this.role = role.toString();
    }
}
