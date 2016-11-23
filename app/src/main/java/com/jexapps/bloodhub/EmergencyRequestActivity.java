package com.jexapps.bloodhub;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_request);

        LinearLayout rlayout = (LinearLayout) findViewById(R.id.activity_emergency_request);
        setTitle("EMERGENCY REQUEST");

    }
}
