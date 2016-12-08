package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by mahnoor on 08/12/2016.
 */

public class AddDonorActivity extends AppCompatActivity{
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
        setContentView(R.layout.activity_add_donor);
        Button addPatient = (Button) findViewById(R.id.add_patient);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(AddDonorActivity.this);
                dialog.setTitle("Add Donor");
                dialog.setContentView(R.layout.popup_donor);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final Button request = (Button) dialog.findViewById(R.id.button_ok);
                request.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddDonorActivity.this,MainActivityOrg.class);
                        intent.putExtra("mEmail", mEmail);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
