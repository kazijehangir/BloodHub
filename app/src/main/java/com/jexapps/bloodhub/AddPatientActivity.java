package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jexapps.bloodhub.m_Model.Patient;

public class AddPatientActivity extends AppCompatActivity {
    Dialog dialog;
    private int date, month, year;
    AutoCompleteTextView name;
    Spinner bloodgroup, diagnosis;
    EditText number, age;
    String pname, bgroup, num, diag, mEmail, a;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mEmail = user.getEmail();
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference().child("patients");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        setTitle("Add Patient");

        name = (AutoCompleteTextView) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        bloodgroup = (Spinner) findViewById(R.id.spin);
        number = (EditText) findViewById(R.id.contact_num);
        diagnosis = (Spinner) findViewById(R.id.diagnosis);

        Button submit = (Button) findViewById(R.id.add_patient);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = getApplicationContext();
                pname = name.getText().toString();
                a = age.getText().toString();
                bgroup = bloodgroup.getSelectedItem().toString();
                diag = diagnosis.getSelectedItem().toString();
                num = number.getText().toString();

                Patient pat = new Patient(user.getUid(),pname, a, bgroup, num, diag);
                try {
                    db.push().setValue(pat);
                    dialog = new Dialog(AddPatientActivity.this);
                    dialog.setTitle("Add Patient");
                    dialog.setContentView(R.layout.popup_patient);
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    final Button submit = (Button) dialog.findViewById(R.id.button_ok);
                    submit.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AddPatientActivity.this,MainActivity.class);
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
