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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.quickstart.database.models.Post;
//import com.google.firebase.quickstart.database.models.User;
import java.util.Arrays;

import static java.lang.Boolean.TRUE;

public class IndividualRegistrationActivity extends AppCompatActivity {
    AutoCompleteTextView username, mEmailView;
    EditText mPasswordView;
    CheckBox mTermsAgree;
    ProgressBar progressBar;
    Spinner bloodGroup;
    String email, password, uname, bgroup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_registration);
        //make the actionbar show arrow to go back to login
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        username = (AutoCompleteTextView) findViewById(R.id.name);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mTermsAgree = (CheckBox) findViewById(R.id.agreeTerms);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bloodGroup = (Spinner) findViewById(R.id.spin);

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

    private void writeNewUser(String userId, String email){
        User user = new User(uname, email, bgroup);
        mDatabase.child("users").child(userId).setValue(user);
    }
    private void registerNewUser() {
//        TODO: check if username, email, password is added or not
        email = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();
        uname = username.getText().toString();
        bgroup = bloodGroup.getSelectedItem().toString();
        if (!email.contains("@")) {
            Toast.makeText(this, "Not a valid email address",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (password.length() < 6) {
                Toast.makeText(this, "Password is too short, minimum length is 6.",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (email.contains(":") || password.contains(":")) {
                    Toast.makeText(this, "Email address and Password cannot contain ':'",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (!mTermsAgree.isChecked()) {
                        Toast.makeText(this, "You need to agree to the terms & conditions to sign up.",
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
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(context, "Registration Successful!",
                                            Toast.LENGTH_SHORT).show();
                                    // take user to main screen
                                    writeNewUser(user.getUid(), user.getEmail());

                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("mEmail", user.getEmail());
                                    startActivity(intent);
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(context, "User with this email already exists.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Error creating user",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                }
                            });
                    }
                }
            }
        }
    }
}
