package com.jexapps.bloodhub;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.jexapps.bloodhub.m_Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrganizationRegistrationActivity extends AppCompatActivity {
    AutoCompleteTextView username;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_registration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        username = (AutoCompleteTextView) findViewById(R.id.name);
        String[] hospitals = getResources().getStringArray(R.array.hospitals);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hospitals);
        username.setAdapter(adapter);
        // Set OnClick Listeners for buttons
        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewOrg();
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
        EditText contactNum = (EditText) findViewById(R.id.contact);
        EditText address = (EditText) findViewById(R.id.add);
        String uname = username.getText().toString();
        String num = contactNum.getText().toString();
        String add = address.getText().toString();
        User user = new User(uname, email, num, add);
        mDatabase.child("users").child(userId).setValue(user);
    }
    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(), user.getEmail());

                            startActivity(new Intent(OrganizationRegistrationActivity.this, LoginActivity.class));

                            finish();
                        }
                        else {
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0 );
                            startActivity(getIntent());
                        }
                    }
                });
    }
    private void registerNewOrg() {
//        TODO: check if username, email, number, address is added or not
        AutoCompleteTextView mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        EditText mPasswordView = (EditText) findViewById(R.id.password);
        CheckBox mTermsAgree = (CheckBox) findViewById(R.id.agreeTerms);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (!email.contains("@")) {
            Toast.makeText(this, "Not a valid email address",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (password.length() < 4) {
                Toast.makeText(this, "Password is too short, minimum length is 4.",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (email.contains(":") || password.contains(":")) {
                    Toast.makeText(this, "Email address and Password cannot contain ':'",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (!mTermsAgree.isChecked()) {
                        Toast.makeText(this, "You need to agree to share your details.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                Context context = getApplicationContext();
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    sendVerificationEmail();
//                    take user to main screen
                                    progressBar.setVisibility(View.GONE);

                                    Intent intent = new Intent(context, LoginActivity.class);
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