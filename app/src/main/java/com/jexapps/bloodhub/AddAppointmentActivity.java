package com.jexapps.bloodhub;

/**
 * Created by mahnoor on 23/11/2016.
 */

import android.annotation.TargetApi;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View.OnClickListener;

import android.icu.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Context;
import android.app.Activity;
import android.widget.Button;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;


public class AddAppointmentActivity extends AppCompatActivity {
    Dialog dialog;
    private int date, month, year, hour, minute;
    private String time;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String mEmail;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mEmail= null;
            } else {
                mEmail= extras.getString("mEmail");
            }
        } else {
            mEmail= (String) savedInstanceState.getSerializable("mEmail");
        }
        setContentView(R.layout.activity_add_appointment);

        final EditText set = (EditText) findViewById(R.id.editText);
        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddAppointmentActivity.this);
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
                        set.setText(date+"-"+month+"-"+year);
                        dialog.cancel();
                    }
                });
            }
        });
        final Spinner spinner = (Spinner) findViewById(R.id.spin1);
        final EditText set1 = (EditText) findViewById(R.id.editText2);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio);
        set1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddAppointmentActivity.this);
                dialog.setTitle("Set Date and Time");
                dialog.setContentView(R.layout.set_time);
                dialog.show();
                final Button setTime = (Button) dialog.findViewById(R.id.set_time);
                final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
                setTime.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        hour = timePicker.getCurrentHour();
                        minute = timePicker.getCurrentMinute();
                        set1.setText(hour+":"+minute);
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
                        intent.putExtra("mEmail", mEmail);
                        int selected_id = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selected_id);
                        intent.putExtra("appointments",spinner.getSelectedItem().toString()+";"+set.getText().toString()+", "+set1.getText().toString()+";"+radioButton.getText().toString());
                        startActivity(intent);
                    }
                });
            }
        });
    }


//    Spinner spinner = (Spinner) findViewById(R.id.spinner);
//    // Create an ArrayAdapter using the string array and a default spinner layout
//    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//            R.array.planets_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//    spinner.setAdapter(adapter);
//    EditText _editText;
//    private int _day;
//    private int _month;
//    private int _birthYear;
//    private Context _context;
//    public MyEditTextDatePicker(Context context, int editTextViewID)
//    {
//        Activity act = (Activity)context;
//        this._editText = (EditText)act.findViewById(editTextViewID);
//        this._editText.setOnClickListener(this);
//        this._context = context;
//    }

//    EditText date;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_appointment);
//        date = (EditText) findViewById(R.id.calendarView);
//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerDialog(AddAppointmentActivity.this, dateD, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
//
//    }
//
//    Calendar myCalendar = Calendar.getInstance();
//    DatePickerDialog.OnDateSetListener dateD = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, monthOfYear);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            updateLabel();
//        }
//    };
//
//    private void updateLabel() {
//        String myFormat = "MM/dd/yy";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
//        date.setText(sdf.format(myCalendar.getTime()));
//    }
}


