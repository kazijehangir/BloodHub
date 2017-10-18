package com.jexapps.bloodhub;

/**
 * Created by mahnoor on 23/11/2016.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.Appointment;
import com.jexapps.bloodhub.m_Model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddAppointmentActivity extends AppCompatActivity {
    Dialog dialog;
    private Spinner spinner;
    private EditText set, set1;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private int day, month, year, hour, minute, position;
    private String time, date;
    private Boolean transport;
    Date pdate;
    ArrayList<String> hospitals;
    ArrayList<String> keys;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        setTitle("Add Appointment");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        spinner = (Spinner) findViewById(R.id.spin1);
        set = (EditText) findViewById(R.id.editText);
        set1 = (EditText) findViewById(R.id.editText2);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

        hospitals = new ArrayList<String>();
        keys = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("account_type").equalTo("organization")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            hospitals.add(user.username);
                            keys.add(child.getKey());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, hospitals);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddAppointmentActivity.this);
                dialog.setTitle("Set Date");
                dialog.setContentView(R.layout.set_date);
                dialog.show();
                final Button setDate = (Button) dialog.findViewById(R.id.set_date);
                final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
                setDate.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        day = datePicker.getDayOfMonth();
                        month = datePicker.getMonth();
                        year = datePicker.getYear();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        pdate = calendar.getTime();
                        date = DateFormat.getDateInstance().format(pdate);
                        set.setText(date);
                        dialog.cancel();
                    }
                });
            }
        });

        set1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddAppointmentActivity.this);
                dialog.setTitle("Set Time");
                dialog.setContentView(R.layout.set_time);
                dialog.show();
                final Button setTime = (Button) dialog.findViewById(R.id.set_time);
                final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
                setTime.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        hour = timePicker.getCurrentHour();
                        minute = timePicker.getCurrentMinute();
                        if (hour >= 12){
                            time = "PM";
                        } else {
                            time = "AM";
                        }
                        if (hour > 12){
                            hour = hour - 12;
                        }
                        if (minute > 10){
                            time = minute + " " + time;
                        } else {
                            time = "0" + minute + " " + time;
                        }
                        if (hour > 10){
                            time = hour + ":" + time;
                        } else {
                            time = "0" + hour + ":" + time;
                        }
                        set1.setText(time);
                        dialog.cancel();
                    }
                });
            }
        });

        Button submit = (Button) findViewById(R.id.submit_button1);
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String transport_text = (String) radioButton.getText();
                if (transport_text.equals("Yes")){
                    transport = true;
                } else {
                    transport = false;
                }
                Appointment appointment = new Appointment(user.getUid(),keys.get(spinner.getSelectedItemPosition()),date,time,transport);
                FirebaseDatabase.getInstance().getReference().child("appointments").push().setValue(appointment);
                dialog = new Dialog(AddAppointmentActivity.this);
                dialog.setTitle("Submit Request");
                dialog.setContentView(R.layout.popup_appointment_submit);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final Button request = (Button) dialog.findViewById(R.id.button_ok);
                request.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddAppointmentActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}


