package com.andrewsosa.android.tru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

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
