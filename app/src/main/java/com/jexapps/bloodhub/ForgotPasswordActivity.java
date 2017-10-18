package com.jexapps.bloodhub;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by aimengaba on 21/07/2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    Dialog dialog;

    AutoCompleteTextView mEmail;
    private View mProgressView;
    private View mEmailFormView;
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
        mEmailFormView = findViewById(R.id.email_form);
        mProgressView = findViewById(R.id.progress_spinner);

        // Set OnClick Listeners for buttons
        Button mVerifyEmailButton = (Button) findViewById(R.id.send_email_button);
        mVerifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                if (email.isEmpty()) {
                    mEmail.setError("Email is empty, please enter the email you used to register.");
                }
                showProgress(true);
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showProgress(false);

                            if (task.isSuccessful()) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Email sent.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mEmailFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmailFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEmailFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mEmailFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
