package com.andrewthewizard.tru.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andrewthewizard.tru.android.R;
import com.andrewthewizard.tru.android.Tru;
import com.andrewthewizard.tru.android.model.MessageModel;
import com.firebase.client.Firebase;

import java.util.Calendar;

public class SendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);


        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onClick(View v) {

        EditText input = (EditText) findViewById(R.id.et_message);
        final String message = input.getText().toString().trim();

        if(message.length() > 0 && message.length()<=90) {


            final Firebase users = new Firebase(Tru.FIREBASE_URL).child("users");
            final String authorID = users.getAuth().getUid();

            final Firebase feedRef = new Firebase(Tru.FIREBASE_URL).child("feed").push();
            feedRef.setValue(new MessageModel(authorID, message));
            feedRef.setPriority(0-Calendar.getInstance().getTimeInMillis());

            users.child(authorID).child("sent")
                    .child(feedRef.getKey())
                    .setValue(new MessageModel(authorID, message));


            Toast.makeText(this, "It's lit, yo", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Not tru, yo", Toast.LENGTH_SHORT).show();
        }


    }
}
