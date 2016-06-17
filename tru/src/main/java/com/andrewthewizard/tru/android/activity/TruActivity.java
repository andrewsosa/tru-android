package com.andrewthewizard.tru.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andrewthewizard.tru.android.Tru;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TruActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            // User is signed in
            Log.d("Tru", "User:signed_in:" + user.getUid());
            intent = new Intent(this, MainActivity.class);
        } else {
            // User is signed out
            Log.d("Tru", "User:signed_out");
            intent = new Intent(this, AuthActivity.class);
        }

        startActivity(intent);
        finish();

    }
}
