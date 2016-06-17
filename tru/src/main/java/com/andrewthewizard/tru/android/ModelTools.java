package com.andrewthewizard.tru.android;

import com.andrewthewizard.tru.android.model.MessageModel;
import com.firebase.client.DataSnapshot;

/**
 * Created by andrewsosa on 2/9/16.
 */
public class ModelTools {

    public static MessageModel fullMessageParse(DataSnapshot snapshot) {
        MessageModel mm = snapshot.getValue(MessageModel.class);
        DataSnapshot hidden = snapshot.child("hidden");
        for(DataSnapshot user : hidden.getChildren()){
            mm.addHiddenUser(user.getKey());
        }
        DataSnapshot agreed = snapshot.child("agreed");
        for(DataSnapshot user: agreed.getChildren()){
            mm.addAgreedUser(user.getKey());
        }
        return mm;
    }
}
