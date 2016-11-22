package com.jexapps.bloodhub;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    private static final String CREDENTIALS_FILE_NAME = "credentials";
    private SharedPreferences CREDENTIAL_FILE;
    private static String[] CREDENTIALS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        TextView name = (TextView) findViewById(R.id.name);
        name.setText("Name: " + credential.split(":")[2]);
        TextView bgroup = (TextView) findViewById(R.id.blood_g);
        bgroup.setText("Blood Group: " + credential.split(":")[3]);
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
