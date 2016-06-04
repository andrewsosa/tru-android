package com.andrewthewizard.tru.android;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {

    private String uid;
    private String username;
    private String email;


    public UserModel() {}

    public UserModel(String uid, String email, String username) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }
}