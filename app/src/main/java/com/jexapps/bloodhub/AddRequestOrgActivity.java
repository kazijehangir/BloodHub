package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddRequestOrgActivity extends AppCompatActivity {
    Dialog dialog;
    int date, month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String mEmail, name, needs, when, diagnosis, status, gender;
        if (savedInstanceState == null) {
//            Toast.makeText(this, "savedInstance == null",
//                    Toast.LENGTH_SHORT).show();
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
//                Toast.makeText(this, "extras == null",
//                        Toast.LENGTH_SHORT).show();
                name = null;
                needs = null;
                when = null;
                diagnosis = null;
                status = null;
                gender = null;
                mEmail = null;
            } else {
//                Toast.makeText(this, "getting strings from extras",
//                        Toast.LENGTH_SHORT).show();
                name = extras.getString("name");
                needs = extras.getString("bgroup");
                when = extras.getString("lastRequest");
                diagnosis = extras.getString("diagnosis");
                status = extras.getString("status");
                gender = extras.getString("gender");
                mEmail = extras.getString("mEmail");
                Toast.makeText(this,gender,Toast.LENGTH_SHORT).show();
            }
        } else {
//            Toast.makeText(this, "getting strings from savedInstance",
//                    Toast.LENGTH_SHORT).show();
            name = (String) savedInstanceState.getSerializable("name");
            needs = (String) savedInstanceState.getSerializable("bgroup");
            when = (String) savedInstanceState.getSerializable("lastRequest");
            diagnosis = (String) savedInstanceState.getSerializable("diagnosis");
            status = (String) savedInstanceState.getSerializable("status");
            gender = (String) savedInstanceState.getSerializable("gender");
            mEmail = (String) savedInstanceState.getSerializable("mEmail");

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request_org);
        setTitle("Add Request");
        TextView mName = (TextView) findViewById(R.id.name);
        mName.setText("Name: "+name);
        TextView mNeeds = (TextView) findViewById(R.id.age);
        mNeeds.setText("Status: "+status);
        TextView mWhen = (TextView) findViewById(R.id.blood_g);
        mWhen.setText("Blood Group: "+needs);
        TextView mDiagnosis = (TextView) findViewById(R.id.con_num);
        mDiagnosis.setText("Diagnosis: "+diagnosis);
        TextView mTransport = (TextView) findViewById(R.id.last);
        mTransport.setText("Last Request: "+when);
        ImageView image = (ImageView) findViewById(R.id.image1);
        if (gender.equals("Female")){
            image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.girl));
        }
        else {
            image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.boy));
        }
        final EditText set = (EditText) findViewById(R.id.editText);
        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddRequestOrgActivity.this);
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
