package com.jexapps.bloodhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.User;

public class UserProfileOrg extends AppCompatActivity  {
    DatabaseReference db;
    private FirebaseAuth mAuth;

    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile);
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
    }

    @Override
    public void onStart() {
        super.onStart();
        final TextView name = (TextView) findViewById(R.id.user_profile_name);
        final TextView number = (TextView) findViewById(R.id.user_profile_number);
        //add value event listener to the user
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User curruser = dataSnapshot.getValue(User.class);
                name.setText("Name: " + curruser.username);
                number.setText("Number : " + curruser.number);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUserReference.addValueEventListener(userListener);
    }
}
