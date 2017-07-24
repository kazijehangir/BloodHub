package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_Model.Donation;

import java.text.DateFormat;
import java.util.Date;

public class RequestDetail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference db;
    Dialog dialog;
    String request, name, needs, location, when, diagnosis, transport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setTitle("Donate blood");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                request = null;
            } else {
                request = extras.getString("request");
            }
        }
        db = FirebaseDatabase.getInstance().getReference().child("donations");
        FirebaseDatabase.getInstance().getReference().child("bloodrequests").child(request)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        BloodRequest data = dataSnapshot.getValue(BloodRequest.class);

                        TextView mName = (TextView) findViewById(R.id.request_detail_name);
                        mName.setText(data.name);
                        TextView mNeeds = (TextView) findViewById(R.id.request_detail_needs);
                        mNeeds.setText(data.quantity+" bags of "+data.blood_group);
                        TextView mLocation = (TextView) findViewById(R.id.request_detail_location);
                        mLocation.setText(data.location);
                        TextView mWhen = (TextView) findViewById(R.id.request_detail_when);
                        String date = DateFormat.getDateInstance().format(new Date(data.date));
                        mWhen.setText(date);
                        if (date.equals(DateFormat.getDateInstance().format(new Date()))) {
                            mWhen.setText("URGENT");
                            mWhen.setTextColor(0xFFFF0000);
                        }
                        TextView mDiagnosis = (TextView) findViewById(R.id.request_detail_diagnosis);
                        mDiagnosis.setText(data.diagnosis);
                        TextView mTransport = (TextView) findViewById(R.id.request_detail_transport);
                        if (data.transport){
                            mTransport.setText("Available");
                        } else {
                            mTransport.setText("Not Available");
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        Button donate = (Button) findViewById(R.id.request_detail_donate_button);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = getApplicationContext();
                Donation donation = new Donation(user.getUid(),request);
                try {
                    db.push().setValue(donation);
                    dialog = new Dialog(RequestDetail.this);
                    dialog.setTitle("Donation Confirmed");
                    dialog.setContentView(R.layout.popup_request_detail);
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    final Button submit = (Button) dialog.findViewById(R.id.request_detail_dialog_ok);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                } catch (DatabaseException e) {
                    Toast.makeText(context,"Error occurred",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
