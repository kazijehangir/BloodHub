package com.jexapps.bloodhub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class EmergencyRequestActivity extends AppCompatActivity {
// TODO: try to get location automatically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_request);

        LinearLayout rlayout = (LinearLayout) findViewById(R.id.activity_emergency_request);
        setTitle("EMERGENCY REQUEST");
    }
}
