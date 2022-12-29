package com.example.yo_flunk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreatePlayerActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String league;
    String linkDatabase;
    int headId = 0;
    int bodyId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);

        Intent main_intent = getIntent();
        league = main_intent.getStringExtra("league");
        linkDatabase = this.getResources().getString(R.string.firebase_link);
    }

    public void SavePlayerToDatabase(View view) {
        EditText editTxtName = findViewById(R.id.name);
        EditText editTxtMotto = findViewById(R.id.motto);
        EditText editTxtBeer = findViewById(R.id.bier);

        String name = editTxtName.getText().toString();
        String motto = editTxtMotto.getText().toString();
        String beer = editTxtBeer.getText().toString();

        if(name.length()<=9) { // only allow players with name length less than 9
            rootNode = FirebaseDatabase.getInstance(linkDatabase);
            reference = rootNode.getReference(league + "/users");

            final Boolean[] playerAdded = {false};
            reference.child(name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) { // name does not exists yet
                        playerAdded[0] = true;
                        UserHelperClass helperClass = new UserHelperClass(name, motto, beer, headId, bodyId);
                        reference.child(name).setValue(helperClass);
                        String text_msg = name + " wurde hinzugefügt.";
                        Toast.makeText(CreatePlayerActivity.this, text_msg, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CreatePlayerActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        if (!playerAdded[0]) {
                            Toast.makeText(CreatePlayerActivity.this, "Dieser Name existiert bereits.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(this, "Bitte nur Namen mit maximal 9 Zeichen eingeben.", Toast.LENGTH_LONG).show();
        }
    }

    public void ChangeHeadLeft(View view) {
        TypedArray heads = getResources().obtainTypedArray(R.array.heads);
        int numHeads = heads.length();
        headId = (((headId - 1) % numHeads) + numHeads) % + numHeads; // just a complicated function to do mod calculation

        ImageView imgViewHead = findViewById(R.id.image_head);
        imgViewHead.setBackgroundResource(heads.getResourceId(headId, 0));
        heads.recycle();
    }

    public void ChangeHeadRight(View view) {
        TypedArray heads = getResources().obtainTypedArray(R.array.heads);
        int numHeads = heads.length();

        headId = (headId + 1) % numHeads;

        ImageView imgViewHead = findViewById(R.id.image_head);
        imgViewHead.setBackgroundResource(heads.getResourceId(headId, 0));
        heads.recycle();
    }

    public void ChangeBodyRight(View view) {
        TypedArray bodies = getResources().obtainTypedArray(R.array.bodies);
        int numBodies = bodies.length();
        bodyId = (bodyId + 1) % numBodies;

        ImageView imgViewBody = findViewById(R.id.image_body);
        imgViewBody.setBackgroundResource(bodies.getResourceId(bodyId, 0));
        bodies.recycle();
    }

    public void ChangeBodyLeft(View view) {
        TypedArray bodies = getResources().obtainTypedArray(R.array.bodies);
        int numBodies = bodies.length();
        bodyId = (((bodyId - 1) % numBodies) + numBodies) % numBodies; //just a very complicated function to compute mod 3

        ImageView imgViewBody = findViewById(R.id.image_body);
        imgViewBody.setBackgroundResource(bodies.getResourceId(bodyId, 0));
        bodies.recycle();
    }
}