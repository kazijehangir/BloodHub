package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddPatientActivity extends AppCompatActivity {
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        Button addPatient = (Button) findViewById(R.id.add_patient);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(AddPatientActivity.this);
                dialog.setTitle("Add Patient");
                dialog.setContentView(R.layout.popup_patient);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final Button request = (Button) dialog.findViewById(R.id.button_ok);
                request.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddPatientActivity.this,MainActivityOrg.class);
                        intent.putExtra("mEmail", mEmail);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
