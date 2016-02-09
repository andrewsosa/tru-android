package com.andrewsosa.android.tru;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageModel {

    String authorKey;
    String contents;
    long value;

    public MessageModel() {}

    public MessageModel(String key, String contents) {
        this.authorKey = key;
        this.contents = contents;
        this.value = 0;
    }

    public String getAuthorKey() {
        return authorKey;
    }

    public String getContents() {
        return contents;
    }

    public long getValue() {
        return value;
    }



    public MessageModel incPoints() {
        value = value + 1;
        return this;
    }


}
