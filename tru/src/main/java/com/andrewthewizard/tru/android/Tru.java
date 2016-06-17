package com.andrewthewizard.tru.android;

import android.app.Application;

import com.firebase.client.Firebase;


public class Tru extends Application {

    public final static String FIREBASE_URL = "https://tru.firebaseio.com";
    public final static String FIREBASE_URL2 = "https://tru-firebase.firebaseio.com";

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

    }


}
