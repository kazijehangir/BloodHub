package com.jexapps.bloodhub;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.User;

import static android.R.attr.duration;

/**
 * Created by mahnoor on 28/11/2016.
 */

public class SplashActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;

    private void setView(String userId, final String email) {
        final String uid = userId;
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String account_type = user.account_type;
//                        showProgress(false);
                        if(account_type.equals("individual")) {
                            Intent intent;
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("mEmail", email);
                            startActivity(intent);
                        } else if(account_type.equals("organization")) {
                            Intent intent;
                            intent = new Intent(SplashActivity.this, MainActivityOrg.class);
                            intent.putExtra("mEmail", email);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        return;
    }
    private void updateUI(FirebaseUser user){
        Context context = getApplicationContext();
        if(user != null){
            Toast toast = Toast.makeText(context, "user", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(context, "No user", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        Context context = getApplicationContext();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//        if(currentUser == null){
//            Toast toast = Toast.makeText(context, "No user", Toast.LENGTH_SHORT);
//            toast.show();
//        } else {
//            Toast toast = Toast.makeText(context, "hir", Toast.LENGTH_SHORT);
//            toast.show();
//        }


        if(currentUser != null){
            setView(currentUser.getUid(), currentUser.getEmail());
        } else {
            setContentView(R.layout.activity_fscreen);
            Button button = (Button) findViewById(R.id.button1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SplashActivity.this,
                            EmergencyRequestActivity.class);
                    startActivity(intent);
                }
            });
            Button button1 = (Button) findViewById(R.id.button2);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

//        Button button = (Button) findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SplashActivity.this,
//                        EmergencyRequestActivity.class);
//                startActivity(intent);
//            }
//        });
//        Button button1 = (Button) findViewById(R.id.button2);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
