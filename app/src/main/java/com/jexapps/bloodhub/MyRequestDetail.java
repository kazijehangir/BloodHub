package com.jexapps.bloodhub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.BloodRequest;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by mahnoor on 19/10/2017.
 */

public class MyRequestDetail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference db;
    String name, when, location, request;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_request_details);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                request = null;
            } else {
                request = extras.getString("request");
            }
        }
        if(request!=null && !request.isEmpty()){
            FirebaseDatabase.getInstance().getReference().child("bloodrequests").child(request).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BloodRequest data = dataSnapshot.getValue(BloodRequest.class);

                    TextView mName = (TextView) findViewById(R.id.name);
                    mName.setText(name);
                    TextView mTime = (TextView) findViewById(R.id.time);
                    mTime.setText(when);
                    TextView mLocation = (TextView) findViewById(R.id.location);
                    mLocation.setText(location);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

    }
}
