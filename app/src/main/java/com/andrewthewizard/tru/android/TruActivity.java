package com.andrewthewizard.tru.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.Firebase;

public class TruActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase ref = new Firebase(Tru.URL);

        Intent intent;


        if(ref.getAuth() == null) {
            intent = new Intent(this, AuthActivity.class);

        } else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();


    }
}
