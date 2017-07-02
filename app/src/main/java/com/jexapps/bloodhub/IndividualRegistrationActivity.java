package com.jexapps.bloodhub;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import static java.lang.Boolean.TRUE;


public class IndividualRegistrationActivity extends AppCompatActivity {
    private static final String CREDENTIALS_FILE_NAME = "credentials";
    private SharedPreferences CREDENTIAL_FILE;
    private static String[] CREDENTIALS;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_registration);
        //make the actionbar show arrow to go back to login
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

// ...
        mAuth = FirebaseAuth.getInstance();
//        Load credentials file so that we can update it.
        CREDENTIAL_FILE = getSharedPreferences(CREDENTIALS_FILE_NAME, 0);
        if (CREDENTIAL_FILE == null) {
            Toast.makeText(this, "Could Not Load Credentials File",
                    Toast.LENGTH_SHORT).show();
        } else {
            int numUsers = CREDENTIAL_FILE.getInt("numUsers", 0);
            CREDENTIALS = new String[numUsers];
            for (int i = 0; i < numUsers; i++)
                CREDENTIALS[i] = CREDENTIAL_FILE.getString("user_" + i, null);
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
    private Boolean emailAlreadyExists(String email) {
        for (String credential : CREDENTIALS) {
//            Toast.makeText(this, "User => " + credential,
//                    Toast.LENGTH_SHORT).show();
            if (credential.split(":")[0].equals(email)) {
                return true;
            }
        }
        return false;

    }
    private void registerNewUser() {
//        TODO: just adding email and password now. Need to add other details
        AutoCompleteTextView mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        EditText mPasswordView = (EditText) findViewById(R.id.password);
        CheckBox mTermsAgree = (CheckBox) findViewById(R.id.agreeTerms);
        AutoCompleteTextView username = (AutoCompleteTextView) findViewById(R.id.name);
        Spinner bloodGroup = (Spinner) findViewById(R.id.spin);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        SharedPreferences.Editor credentials_edit = CREDENTIAL_FILE.edit();

        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        Toast.makeText(this, email+" " +password,
                Toast.LENGTH_SHORT).show();
        String uname = username.getText().toString();
        String bgroup = bloodGroup.getSelectedItem().toString();
        if (!mTermsAgree.isChecked()) {
            Toast.makeText(this, "You need to agree to the terms & conditions to sign up.",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (emailAlreadyExists(email)) {
                Toast.makeText(this, "User with this Email address already exists.",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (email.contains(":") || password.contains(":")) {
                    Toast.makeText(this, "Email address and Password cannot contain ':'",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (password.length() < 6) {
                        Toast.makeText(this, "Password is too short, minimum length is 4.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (!email.contains("@")) {
                            Toast.makeText(this, "Not a valid email address",
                                    Toast.LENGTH_SHORT).show();
                        } else {
//                    register user with firebase
                            progressBar.setVisibility(View.VISIBLE);
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Context context = getApplicationContext();
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
//                                                Log.d(TAG, "createUserWithEmail:success");

                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Toast.makeText(context, "Registration Successful!",
                                                        Toast.LENGTH_SHORT).show();
//                    take user to main screen
                                                progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(context, MainActivity.class);
                                                intent.putExtra("mEmail", user.getEmail());
                                                startActivity(intent);
//                                              updateUI(user);
                                            } else {
                                                // If sign in f
                                                //
                                                // .ails, display a message to the user.
//                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                                Toast.makeText(context, "Error creating user",
                                                        Toast.LENGTH_SHORT).show();
//                                                updateUI(null);
                                            }
                                        }
                                    });
//                            int numUsers = CREDENTIAL_FILE.getInt("numUsers", 0);
//                            credentials_edit.putString("user_" + numUsers, email + ":" + password
//                                                        + ":" + uname + ":" + bgroup + ":" + "ind");
//                            credentials_edit.putInt("numUsers", numUsers + 1);
//                            credentials_edit.commit();
//                    add user's name to name file
//                    registration successful, show success popup

                        }
                    }
                }
            }
        }
//        TESTS: comment out later
//        int numUsers = CREDENTIAL_FILE.getInt("numUsers", 0);
//        String[] CREDENTIALS = new String[numUsers];
//        for (int i = 0; i < numUsers; i++) {
//            CREDENTIALS[i] = CREDENTIAL_FILE.getString("user_" + i, null);
//        }
//        //        Test to see which users are there
//        for (String credential : CREDENTIALS) {
//            Toast.makeText(this, "User => " + credential,
//            Toast.LENGTH_SHORT).show();
//        }
//        END OF TESTS
    }
}
