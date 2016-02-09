package com.andrewsosa.android.tru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btn_true) {
            EditText username = (EditText) findViewById(R.id.et_username);
            EditText password = (EditText) findViewById(R.id.et_password);

            final String user = username.getText().toString();
            final String pass = password.getText().toString();

            //Firebase ref = new Firebase(Tru.URL);
            //Firebase users = ref.child("users");

            signup(user, pass);
        }
    }

    public void login(final String user, String pass) {
        final Firebase ref = new Firebase(Tru.URL);
        final String x = user + "@tru.com";
        ref.authWithPassword(x, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {

                Firebase users = ref.child("users");



                Firebase xref = users.child(authData.getUid()); //.setValue(new UserModel(authData.getUid(), user));

                Map<String, Object> xmap = new HashMap<String, Object>();
                xmap.put("uid", authData.getUid());
                xmap.put("username", user);
                xref.updateChildren(xmap);
                
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(AuthActivity.this, "Not tru, yo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signup(final String user, final String pass) {

        Firebase ref = new Firebase(Tru.URL);
        ref.createUser(user + "@tru.com", pass, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                login(user, pass);
            }

            @Override
            public void onError(FirebaseError firebaseError) {

                if(firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                    login(user, pass);
                } else {
                    Toast.makeText(AuthActivity.this, "Not tru, yo", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
