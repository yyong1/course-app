package com.example.coursesystem.rest.dto;

public class JwtGetDTO {
    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public JwtGetDTO(String jwt) {
        this.jwt = jwt;
    }
}
