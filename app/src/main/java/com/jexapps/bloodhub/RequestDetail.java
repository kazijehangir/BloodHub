package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RequestDetail extends AppCompatActivity {
    Dialog dialog;
    String mEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String name, needs, location, when, diagnosis, transport;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        setTitle("Donate blood");
        if (savedInstanceState == null) {
//            Toast.makeText(this, "savedInstance == null",
//                    Toast.LENGTH_SHORT).show();
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
//                Toast.makeText(this, "extras == null",
//                        Toast.LENGTH_SHORT).show();
                name = null;
                needs = null;
                location = null;
                when = null;
                diagnosis = null;
                transport = null;
                mEmail = null;
            } else {
//                Toast.makeText(this, "getting strings from extras",
//                        Toast.LENGTH_SHORT).show();
                name = extras.getString("name");
                needs = extras.getString("needs");
                location = extras.getString("location");
                when = extras.getString("when");
                diagnosis = extras.getString("diagnosis");
                transport = extras.getString("transport");
                mEmail = extras.getString("mEmail");
            }
        } else {
//            Toast.makeText(this, "getting strings from savedInstance",
//                    Toast.LENGTH_SHORT).show();
            name = (String) savedInstanceState.getSerializable("name");
            needs = (String) savedInstanceState.getSerializable("needs");
            location = (String) savedInstanceState.getSerializable("location");
            when = (String) savedInstanceState.getSerializable("when");
            diagnosis = (String) savedInstanceState.getSerializable("diagnosis");
            transport = (String) savedInstanceState.getSerializable("transport");
            mEmail = (String) savedInstanceState.getSerializable("mEmail");

        }
        TextView mName = (TextView) findViewById(R.id.request_detail_name);
        mName.setText(name);
        TextView mNeeds = (TextView) findViewById(R.id.request_detail_needs);
        mNeeds.setText(needs);
        TextView mLocation = (TextView) findViewById(R.id.request_detail_location);
        mLocation.setText(location);
        TextView mWhen = (TextView) findViewById(R.id.request_detail_when);
        mWhen.setText(when);
        TextView mDiagnosis = (TextView) findViewById(R.id.request_detail_diagnosis);
        mDiagnosis.setText(diagnosis);
        TextView mTransport = (TextView) findViewById(R.id.request_detail_transport);
        mTransport.setText(transport);

        Button donate = (Button) findViewById(R.id.request_detail_donate_button);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(RequestDetail.this);
                dialog.setTitle("Donation Confirmed");
                dialog.setContentView(R.layout.popup_request_detail);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final Button request = (Button) dialog.findViewById(R.id.request_detail_dialog_ok);
                request.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(RequestDetail.this,MainActivity.class);
//                        intent.putExtra("mEmail", mEmail);
//                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
