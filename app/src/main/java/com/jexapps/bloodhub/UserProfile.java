package com.jexapps.bloodhub;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.User;

public class UserProfile extends AppCompatActivity  {
    private static final String CREDENTIALS_FILE_NAME = "credentials";
    private SharedPreferences CREDENTIAL_FILE;
    private static String[] CREDENTIALS;
    DatabaseReference db;
    private FirebaseAuth mAuth;

    private DatabaseReference mUserReference;
    private ValueEventListener mUserListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        final String mEmail;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mEmail= null;
            } else {
                mEmail= extras.getString("mEmail");
            }
        } else {
            mEmail= (String) savedInstanceState.getSerializable("mEmail");
        }
        String credential = getInfoFromDatabase(mEmail);
//        TextView name = (TextView) findViewById(R.id.name);
//        name.setText("Name: " + user.getDisplayName());
//        TextView bgroup = (TextView) findViewById(R.id.blood_g);
//        bgroup.setText("Blood Group: " + db.child(user.getUid()));
    }

    @Override
    public void onStart() {
        super.onStart();
        final TextView name = (TextView) findViewById(R.id.name);
        final TextView bgroup = (TextView) findViewById(R.id.blood_g);

        //add value event listener to the user
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User curruser = dataSnapshot.getValue(User.class);
                name.setText("Name: " + curruser.username);
                bgroup.setText("Blood group: " + curruser.blood_group);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUserReference.addValueEventListener(userListener);
        mUserListener = userListener;
    }
    private String getInfoFromDatabase(String email) {
        CREDENTIAL_FILE = getSharedPreferences(CREDENTIALS_FILE_NAME, 0);
        int numUsers = CREDENTIAL_FILE.getInt("numUsers", 0);
        CREDENTIALS = new String[numUsers];
        for (int i = 0; i < numUsers; i++)
            CREDENTIALS[i] = CREDENTIAL_FILE.getString("user_" + i, null);

        for (String credential : CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(email)) {
                // Account exists, return true if the password matches.
                return credential;
            }
        }
        return null;
    }
}
