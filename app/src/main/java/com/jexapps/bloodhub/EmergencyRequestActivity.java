package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jexapps.bloodhub.m_Model.BloodRequest;

import java.text.DateFormat;
import java.util.Date;

public class EmergencyRequestActivity extends AppCompatActivity {
// TODO: try to get location automatically
    Dialog dialog;
    AutoCompleteTextView name;
    Spinner bloodgroup, quantity, diagnosis, location;
    EditText number;
    String pname, bgroup, quan, diag, num, loc, date, mEmail;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_request);
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference().child("bloodrequests");
        setTitle("Emergency Request");

        name = (AutoCompleteTextView) findViewById(R.id.name);
        bloodgroup = (Spinner) findViewById(R.id.spin);
        quantity = (Spinner) findViewById(R.id.spin1);
        number = (EditText) findViewById(R.id.contact_num);
        location = (Spinner) findViewById(R.id.spin2);
        diagnosis = (Spinner) findViewById(R.id.diagnosis);

        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = getApplicationContext();
                pname = name.getText().toString();
                bgroup = bloodgroup.getSelectedItem().toString();
                quan = quantity.getSelectedItem().toString();
                diag = diagnosis.getSelectedItem().toString();
                num = number.getText().toString();
                loc = location.getSelectedItem().toString();
                BloodRequest request = new BloodRequest(null, pname, bgroup, quan, num, loc, diag, new Date().getTime(), true);
                try {
                    db.push().setValue(request);
                    dialog = new Dialog(EmergencyRequestActivity.this);
                    dialog.setTitle("Submit Request");
                    dialog.setContentView(R.layout.popup_submit);
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    final Button submit = (Button) dialog.findViewById(R.id.button_ok);
                    submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(EmergencyRequestActivity.this, SplashActivity.class);
                            startActivity(intent);
                        }
                    });
                } catch (DatabaseException e) {
                    Toast.makeText(context,"Error occurred",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
