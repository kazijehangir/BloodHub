package com.jexapps.bloodhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by mahnoor on 19/10/2017.
 */

public class MyRequestDetail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference db;
    String name, when, location;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_request_details);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(savedInstanceState == null){

        } else {
            name = (String) savedInstanceState.getSerializable("name");
            when = (String) savedInstanceState.getSerializable("when");
            location = (String) savedInstanceState.getSerializable("location");
        }
        TextView mName = (TextView) findViewById(R.id.name);
        mName.setText(name);
        TextView mTime = (TextView) findViewById(R.id.time);
        mTime.setText(when);
        TextView mLocation = (TextView) findViewById(R.id.location);
        mLocation.setText(location);
    }
}
