package com.andrewsosa.android.tru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

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

        if(message.length() >0) {


            final Firebase users = new Firebase(Tru.URL).child("users");

            final Firebase outbox = users.child(users.getAuth().getUid()).child("sent").push();
            outbox.setValue(new MessageModel(users.getAuth().getUid(), message));

            users.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (!dataSnapshot.getRef().getKey().equals(users.getAuth().getUid())) {
                        Firebase ref = dataSnapshot.getRef().child("inbox").child(outbox.getKey());
                        ref.setValue(new MessageModel(ref.getAuth().getUid(), message));
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            Toast.makeText(this, "It's lit, yo", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Not tru, yo", Toast.LENGTH_SHORT).show();
        }


    }
}
