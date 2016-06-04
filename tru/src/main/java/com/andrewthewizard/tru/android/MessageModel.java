package com.andrewthewizard.tru.android;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Calendar;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageModel {

    private String authorID;
    private String contents;
    private HashMap<String,Boolean> dismissed = new HashMap<>();
    private HashMap<String,Boolean> agreed = new HashMap<>();
    private long timestamp;
    private long order;

    public long value;


    public MessageModel() {}

    public MessageModel(String key, String contents) {
        this.authorID = key;
        this.contents = contents;
        this.value = 0;
        this.timestamp = Calendar.getInstance().getTime().getTime();
        this.order = 0 - timestamp;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getContents() {
        return contents;
    }

    public long getValue() {
        return value;
    }

    public void addHiddenUser(String key) {
        dismissed.put(key, true);
    }

    public boolean ignoredBy(String key) {
        return dismissed.containsKey(key);
    }

    public void addAgreedUser(String key) {
        agreed.put(key, true);
    }

    public boolean agreedBy(String key) {
        return agreed.containsKey(key);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getOrder() {
        return order;
    }
}
