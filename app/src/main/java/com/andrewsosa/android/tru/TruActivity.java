package com.andrewsosa.android.tru;

import android.content.Intent;
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

        if(ref.getAuth() == null) {
            ref.authAnonymously(new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // we've authenticated this session with your Firebase app
                    startActivity(new Intent(TruActivity.this, MainActivity.class));
                    finish();

                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                    Toast.makeText(TruActivity.this, "Unable to connect", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        } else {
            Intent intent;
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }


    }
}
