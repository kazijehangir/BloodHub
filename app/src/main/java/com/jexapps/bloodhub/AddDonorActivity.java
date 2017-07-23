package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jexapps.bloodhub.m_Model.Donor;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mahnoor on 08/12/2016.
 */

public class AddDonorActivity extends AppCompatActivity{
    Dialog dialog;
    int date, month, year;
    AutoCompleteTextView name;
    Spinner bloodgroup;
    EditText age, number, address, donorOrigin, lastDonated;
    String dName, dBloodgroup, dAge, dNumber, dAddress, ddonorOrigin, dLastDonated;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    Date pdate;
    DatabaseReference db;
    String mEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mEmail = user.getEmail();
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference().child("donors");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donor);
        setTitle("Add Donor");

        name = (AutoCompleteTextView) findViewById(R.id.name);
        bloodgroup = (Spinner) findViewById(R.id.spin);
        age = (EditText) findViewById(R.id.age);
        number = (EditText) findViewById(R.id.editText2);
        address = (EditText) findViewById(R.id.editText3);
        donorOrigin = (EditText) findViewById(R.id.editText4);
        lastDonated = (EditText) findViewById(R.id.editText);
        final EditText set = (EditText) findViewById(R.id.editText);
        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddDonorActivity.this);
                dialog.setTitle("Set Date and Time");
                dialog.setContentView(R.layout.set_date);
                dialog.show();
                final Button setDate = (Button) dialog.findViewById(R.id.set_date);
                final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                setDate.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        date = datePicker.getDayOfMonth();
                        month = datePicker.getMonth();
                        year = datePicker.getYear();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, date);
                        pdate = calendar.getTime();
                        String date = DateFormat.getDateInstance().format(pdate);
                        set.setText(date);
                        dialog.cancel();
                    }
                });
            }
        });
        Button addDonor = (Button) findViewById(R.id.add_donor);
        addDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = getApplicationContext();
                dName = name.getText().toString();
                dBloodgroup = bloodgroup.getSelectedItem().toString();
                dAge = age.getText().toString();
                dNumber = number.getText().toString();
                dAddress = address.getText().toString();
                ddonorOrigin = donorOrigin.getText().toString();
                dLastDonated = lastDonated.getText().toString();

                Donor donor = new Donor(user.getUid(), dName, dBloodgroup, dNumber, dAddress, dLastDonated, ddonorOrigin, dAge);
                try {
                    db.push().setValue(donor);
                    dialog = new Dialog(AddDonorActivity.this);
                    dialog.setTitle("Submit Request");
                    dialog.setContentView(R.layout.popup_donor);
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    final Button submit = (Button) dialog.findViewById(R.id.add_donor);
                    submit.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AddDonorActivity.this,MainActivityOrg.class);
                            startActivity(intent);
                        }
                    });
                } catch (DatabaseException e) {
                    Toast.makeText(context,"Error occurred",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
