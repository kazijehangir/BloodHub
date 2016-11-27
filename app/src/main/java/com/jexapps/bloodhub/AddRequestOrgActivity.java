package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddRequestOrgActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_add_request_org);

        Button add = (Button) findViewById(R.id.add_patient);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(AddRequestOrgActivity.this);
                dialog.setTitle("Add Requestt");
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
