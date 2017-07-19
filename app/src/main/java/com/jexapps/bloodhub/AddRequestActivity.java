package com.jexapps.bloodhub;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jexapps.bloodhub.m_Helper.RequestHelper;
import com.jexapps.bloodhub.m_Model.BloodRequest;


public class AddRequestActivity extends AppCompatActivity{
    Dialog dialog;
    private int date, month, year;
    AutoCompleteTextView name;
    Spinner bloodgroup, quantity, diagnosis;
    EditText number, location, when;
    String pname, bgroup, quan, diag, pdate, num, loc, mEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference db;
    RequestHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mEmail = user.getEmail();
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference();
        helper = new RequestHelper(db);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        setTitle("Add Request");

        name = (AutoCompleteTextView) findViewById(R.id.name);
        bloodgroup = (Spinner) findViewById(R.id.spin);
        quantity = (Spinner) findViewById(R.id.spin1);
        number = (EditText) findViewById(R.id.contact_num);
        location = (EditText) findViewById(R.id.loc);
        when = (EditText) findViewById(R.id.editText);
        diagnosis = (Spinner) findViewById(R.id.diagnosis);

        final EditText set = (EditText) findViewById(R.id.editText);
        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddRequestActivity.this);
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

        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = getApplicationContext();
                pname = name.getText().toString();
                bgroup = bloodgroup.getSelectedItem().toString();
                quan = quantity.getSelectedItem().toString();
                diag = diagnosis.getSelectedItem().toString();
                pdate = when.getText().toString();
                num = number.getText().toString();
                loc = location.getText().toString();

                BloodRequest request = new BloodRequest(user.getUid(), pname, bgroup, quan, num, loc, diag, pdate);
                if (helper.save(request)) {
                    dialog = new Dialog(AddRequestActivity.this);
                    dialog.setTitle("Submit Request");
                    dialog.setContentView(R.layout.popup_submit);
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    final Button submit = (Button) dialog.findViewById(R.id.button_ok);
                    submit.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AddRequestActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(context,"Error occurred",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
