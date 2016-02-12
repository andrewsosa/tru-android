package com.andrewsosa.android.tru;

import android.app.Application;

import com.firebase.client.Firebase;


public class Tru extends Application {

    public final static String URL = "https://tru.firebaseio.com";

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

    }


}
