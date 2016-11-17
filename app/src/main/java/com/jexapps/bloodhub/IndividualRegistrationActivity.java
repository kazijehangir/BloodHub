package com.jexapps.bloodhub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;


public class IndividualRegistrationActivity extends AppCompatActivity {
//    TODO: Add user records to credentials storage when registration is successful.
    private static final String CREDENTIALS_FILE_NAME = "credentials";
    private static String[] CREDENTIALS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_registration);
        //make the actionbar show arrow to go back to login
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        Load credentials file so that we can update it.
        SharedPreferences CREDENTIALS_FILE = getSharedPreferences(CREDENTIALS_FILE_NAME, 0);
        if (CREDENTIALS_FILE == null) {
            Toast.makeText(this, "Could Not Load Credentials File",
                    Toast.LENGTH_SHORT).show();
        } else {
            int numUsers = CREDENTIALS_FILE.getInt("numUsers", 0);
            CREDENTIALS = new String[numUsers];
            for (int i = 0; i < numUsers; i++)
                CREDENTIALS[i] = CREDENTIALS_FILE.getString("user_" + i, null);
        }
        // Set OnClick Listeners for buttons
        Button mRegisterButton = (Button) findViewById(R.id.register_individual_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void registerNewUser() {
//        TODO: just adding email and password now. Need to add other details
        AutoCompleteTextView mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        EditText mPasswordView = (EditText) findViewById(R.id.password);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
//        CheckBox = (CheckBox) findViewById(R.id.agreeTerms);
    }

}
