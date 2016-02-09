package com.andrewsosa.android.tru;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by andrewsosa on 10/21/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {

    private String uid;
    private String username;


    public UserModel() {}

    public UserModel(String uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

}