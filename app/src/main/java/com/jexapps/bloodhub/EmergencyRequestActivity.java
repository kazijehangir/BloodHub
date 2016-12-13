package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class EmergencyRequestActivity extends AppCompatActivity {
// TODO: try to get location automatically
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_request);
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
        setTitle("EMERGENCY REQUEST");

        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(EmergencyRequestActivity.this);
                dialog.setTitle("Submit Request");
                dialog.setContentView(R.layout.popup_submit);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final Button request = (Button) dialog.findViewById(R.id.button_ok);
                request.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EmergencyRequestActivity.this,SplashActivity.class);
                        intent.putExtra("mEmail", mEmail);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
