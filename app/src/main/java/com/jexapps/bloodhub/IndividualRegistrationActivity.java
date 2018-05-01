package com.jexapps.bloodhub;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jexapps.bloodhub.m_Model.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class IndividualRegistrationActivity extends AppCompatActivity {
    ImageButton photo_btn;
    AutoCompleteTextView username, mEmailView;
    EditText mPasswordView, mContactView;
    Spinner bloodGroup;
    Uri image_file;
    String email, password, uname, bgroup, contact;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private static final int GALLERY_INTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_registration);
        setTitle("Individual Registration");
        //make the actionbar show arrow to go back to login
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Sending email", Toast.LENGTH_SHORT);
                    toast.show();
                    sendVerificationEmail();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not sending email", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        username = (AutoCompleteTextView) findViewById(R.id.name);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mContactView = (EditText) findViewById(R.id.number);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bloodGroup = (Spinner) findViewById(R.id.spin);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(bloodGroup);
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }
        List<String> bgroups = Arrays.asList(getResources().getStringArray(R.array.blood_groups));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text_view, bgroups) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                v.setPadding(8, 8, 8, 15);
                if (position == getCount()) {
                    ((TextView)v.findViewById(R.id.text1)).setText("");
                    ((TextView)v.findViewById(R.id.text1)).setHint(getItem(getCount()));
                }
                return v;
            }
            @Override
            public int getCount() {
                return super.getCount()-1;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_text_view);
        bloodGroup.setAdapter(adapter);
        bloodGroup.setSelection(adapter.getCount());

        // Set OnClick Listeners for buttons
        photo_btn = (ImageButton) findViewById(R.id.profile_photo);
        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_individual_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            image_file = data.getData();
            InputStream is;
            Drawable icon = getResources().getDrawable(R.drawable.user_default);
            try {
                is = this.getContentResolver().openInputStream(image_file);
//                BitmapFactory.Options options=new BitmapFactory.Options();
//                options.inSampleSize = 10;
//                Bitmap preview_bitmap=BitmapFactory.decodeStream(is,null,options);
                icon = Drawable.createFromStream(is, image_file.toString() );
            } catch (FileNotFoundException e) {
//                icon = getResources().getDrawable(R.drawable.shopping1);
                Toast.makeText(this, "Heree",Toast.LENGTH_SHORT).show();
            }
            photo_btn.setImageDrawable(icon);
//            photo_btn.setImageBitmap(bm);
        }
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
    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "sent email", Toast.LENGTH_SHORT);
                        if(task.isSuccessful()){
                            FirebaseAuth.getInstance().signOut();
//                            startActivity(new Intent(IndividualRegistrationActivity.this, LoginActivity.class));
                            finish();
                            Toast.makeText(getApplicationContext(), "sent email success", Toast.LENGTH_SHORT);
                        }
                        else {
                            overridePendingTransition(0, 0);
                            finish();
//                            overridePendingTransition(0, 0 );
//                            startActivity(getIntent());
                            Toast.makeText(getApplicationContext(), "sent email failed", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
    private void writeNewUser(String userId, String email){
        User user = new User(uname, email, contact, bgroup, "individual");
        mDatabase.child("users").child(userId).setValue(user);
    }
    private boolean isEmailValid(String email) {
        return email.matches("(.+)(@)(.+)(\\.)(.+)");
    }
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
    private boolean isContactValid(String contact){return ((contact.startsWith("+923") && contact.length() == 13) ||
                (contact.startsWith("03") && contact.length() == 11));
    }
    private void registerNewUser() {
        email = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();
        uname = username.getText().toString();
        bgroup = bloodGroup.getSelectedItem().toString();
        contact = mContactView.getText().toString().trim();
        if (uname.isEmpty()) {
            Toast.makeText(this, "You forgot to enter your name.",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (!isEmailValid(email)) {
                Toast.makeText(this, "Not a valid email address",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (!isPasswordValid(password)) {
                    Toast.makeText(this, "Password is too short, minimum length is 6.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (bgroup.contentEquals("Blood Group")) {
                        Toast.makeText(this, "You forgot to choose your blood group.",
                                Toast.LENGTH_SHORT).show();
                    } else {
//                    register user with firebase
//                    progressBar.setVisibility(View.VISIBLE);
                        if(!isContactValid(contact)){
                            Toast.makeText(this, "Please enter a valid number in the +923XX XXXXXXX or 03XX XXXXXXX Format",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Context context = getApplicationContext();
//                                    progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Toast.makeText(context, "Registration Successful!",
                                                        Toast.LENGTH_SHORT).show();
                                                //set default subscriptions
                                                int id = Arrays.asList(getResources().getStringArray(R.array.blood_groups)).indexOf(bgroup);
                                                FirebaseMessaging.getInstance().subscribeToTopic("Request_"+id);
                                                FirebaseMessaging.getInstance().subscribeToTopic("URGENT");
                                                // take user to main screen
                                                sendVerificationEmail();
                                                writeNewUser(user.getUid(), user.getEmail());

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
}
