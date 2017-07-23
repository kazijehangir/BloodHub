package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
    CharSequence options[] = new CharSequence[] {"Never", "Set date"};

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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDonorActivity.this);
                builder.setTitle("Last donated");
                builder.setItems(options, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if(options[which] == "Set date"){
                            final Dialog dialog1;
                            dialog1 = new Dialog(AddDonorActivity.this);
                            dialog1.setTitle("Set Date and Time");
                            dialog1.setContentView(R.layout.set_date);
                            dialog1.show();
                            final Button setDate = (Button) dialog1.findViewById(R.id.set_date);
                            final DatePicker datePicker = (DatePicker) dialog1.findViewById(R.id.datePicker);
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
                                    dialog1.cancel();
                                }
                            });
                        } else {
                            set.setText("Never");
                        }
                    }
                });
                builder.show();

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
                View focusView = null;
                if(TextUtils.isEmpty(dName)){
                    name.setError("Name cannot be empty");
                    focusView = name;
                } else if(TextUtils.isEmpty(dNumber)) {
                    number.setError("Number cannot be empty");
                }
                else {
                    Donor donor = new Donor(user.getUid(), dName, dBloodgroup, dNumber, dAddress, dLastDonated, ddonorOrigin, dAge);
                    try {
                        db.push().setValue(donor);
                        dialog = new Dialog(AddDonorActivity.this);
                        dialog.setTitle("Add Donor");
                        dialog.setContentView(R.layout.popup_donor);
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        final Button submit = (Button) dialog.findViewById(R.id.button_ok);
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
//                else if(dBloodgroup) {
//                    bloodgroup.setError("Blood group can not be empty");
//                }

            }
        });
    }
}
