package com.jexapps.bloodhub;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by aimengaba on 21/07/2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    Dialog dialog;

    AutoCompleteTextView mEmail;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("D","INNNN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_enter_email);
        //INITIALIZE FIREBASE DB

        mAuth = FirebaseAuth.getInstance();

        mEmail = (AutoCompleteTextView) findViewById(R.id.email);

        // Set OnClick Listeners for buttons
        Button mVerifyEmailButton = (Button) findViewById(R.id.verify_email_button);

    }

}
