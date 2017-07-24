package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.Patient;

public class AddRequestOrgActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference db;

    Dialog dialog;
    int date, month, year;
    String mEmail, patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request_org);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setTitle("Add Request for Patient");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                patient = null;
            } else {
                patient = extras.getString("patient");
            }
        }
//                name = null;
//                age = null;
//                needs = null;
//                when = null;
//                diagnosis = null;
//                gender = null;
//                mEmail = null;
//            } else {
////
//                name = extras.getString("name");
//                age = extras.getString("age");
//                needs = extras.getString("bgroup");
//                when = extras.getString("lastRequest");
//                diagnosis = extras.getString("diagnosis");
//                gender = extras.getString("gender");
//                mEmail = extras.getString("mEmail");
//                Toast.makeText(this,gender,Toast.LENGTH_SHORT).show();
//            }
//        } else {
////            Toast.makeText(this, "getting strings from savedInstance",
////                    Toast.LENGTH_SHORT).show();
//            name = (String) savedInstanceState.getSerializable("name");
//            age = (String) savedInstanceState.getSerializable("age");
//            needs = (String) savedInstanceState.getSerializable("bgroup");
//            when = (String) savedInstanceState.getSerializable("lastRequest");
//            diagnosis = (String) savedInstanceState.getSerializable("diagnosis");
//            gender = (String) savedInstanceState.getSerializable("gender");
//            mEmail = (String) savedInstanceState.getSerializable("mEmail");
//
//        }
//
////        TextView mName = (TextView) findViewById(R.id.name);
////        mName.setText("Name: "+name);
////        TextView mAge = (TextView) findViewById(R.id.age);
////        mName.setText("Age: "+age);
////        TextView mWhen = (TextView) findViewById(R.id.blood_g);
////        mWhen.setText("Blood Group: "+needs);
////        TextView mDiagnosis = (TextView) findViewById(R.id.con_num);
////        mDiagnosis.setText("Diagnosis: "+diagnosis);
////        TextView mTransport = (TextView) findViewById(R.id.last);
//        mTransport.setText("Last Request: "+when);
//        ImageView image = (ImageView) findViewById(R.id.image1);
//        if (gender.equals("Female")){
//            image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.girl));
//        }
//        else {
//            image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.boy));
//        }
        //db = FirebaseDatabase.getInstance().getReference().child("patient details");
        FirebaseDatabase.getInstance().getReference().child("patients").child(patient)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Patient data = dataSnapshot.getValue(Patient.class);

                        TextView mName = (TextView) findViewById(R.id.name);
                        mName.setText("Name: " + data.name);

                        TextView mAge = (TextView) findViewById(R.id.age);
                        mAge.setText("Age: " + data.age);

                        TextView mCnumber = (TextView) findViewById(R.id.cnum);
                        mCnumber.setText("Contact Number: " + data.cnumber);

                        TextView mBloodgroup = (TextView) findViewById(R.id.blood_g);
                        mBloodgroup.setText("Blood Group: " + data.blood_group);

                        TextView mDiagnosis = (TextView) findViewById(R.id.con_num);
                        mDiagnosis.setText("Diagnosis: " + data.diagnosis);

//                        Spinner mNeeds = (Spinner) findViewById(R.id.spin1);
//                        mNeeds.getSelectedItem().toString();

//
//                        TextView mLocation = (TextView) findViewById(R.id.request_detail_location);
//                        mLocation.setText(data.location);
//                        TextView mWhen = (TextView) findViewById(R.id.request_detail_when);
//                        String date = DateFormat.getDateInstance().format(new Date(data.date));
//                        mWhen.setText(date);
//                        if (date.equals(DateFormat.getDateInstance().format(new Date()))) {
//                            mWhen.setText("URGENT");
//                            mWhen.setTextColor(0xFFFF0000);
//                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        Button add = (Button) findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(AddRequestOrgActivity.this);
                dialog.setTitle("Add Request");
                dialog.setContentView(R.layout.popup_submit);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final Button request = (Button) dialog.findViewById(R.id.button_ok);
                request.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddRequestOrgActivity.this,MainActivityOrg.class);
                        intent.putExtra("mEmail", mEmail);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
