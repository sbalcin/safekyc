package com.company.safekyc.payload;

import java.time.Instant;

public class UserProfile {
    private Long id;
    private String username;
    private Instant joinedAt;
    private String email;
    private String phone;
    private String website;

    public UserProfile(){

    }

    public UserProfile(Long id, String username, Instant joinedAt, String email, String phone, String website) {
        this.id = id;
        this.username = username;
        this.joinedAt = joinedAt;
        this.email = email;
        this.phone = phone;
        this.website = website;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
