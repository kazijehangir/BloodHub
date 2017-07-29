package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_Model.Patient;
import com.jexapps.bloodhub.m_UI.HttpDataHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.jexapps.bloodhub.R.id.location;


public class AddRequestOrgActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db, new_request;
    private StorageReference mStorageRef;

    AutoCompleteTextView location;
    RadioGroup transport_group;
    RadioButton transport_btn;
    EditText when;
    Spinner quantity;

    double lat = -1, lng = -1;
    Boolean request_added = false;

    String pname, bgroup, quan, diag, num, loc, pEmail;
    Date pdate;
    Boolean transport;

    Dialog dialog;
    int date, month, year;
    String mEmail, patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mEmail = user.getEmail();
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference().child("bloodrequests");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("bloodrequests");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request_org);
        setTitle("Add Request for Patient");


        location = (AutoCompleteTextView) findViewById(R.id.loc);
        when = (EditText) findViewById(R.id.editText);
        transport_group = (RadioGroup) findViewById(R.id.transport);
        quantity = (Spinner) findViewById(R.id.spin1);

        String[] hospitals = getResources().getStringArray(R.array.hospitals);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hospitals);
        location.setAdapter(adapter);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                patient = null;
            } else {
                patient = extras.getString("patient");
            }
        }
        final EditText set = (EditText) findViewById(R.id.editText);
        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddRequestOrgActivity.this);
                dialog.setTitle("Set Date and Time");
                dialog.setContentView(R.layout.set_date);
                dialog.show();
                final Button setDate = (Button) dialog.findViewById(R.id.set_date);
                final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                setDate.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        date = datePicker.getDayOfMonth();
                        month = datePicker.getMonth();
                        year = datePicker.getYear();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, date);
                        pdate = calendar.getTime();
                        String date = DateFormat.getDateInstance().format(pdate);
                        set.setText(date);
                        dialog.cancel();
                    }
                });
            }
        });
//                name = null;
//                age = null;
//                needs = null;
//                when = null;
//                diagnosis = null;
//                gender = null;
//                mEmail = null;
//            } else {
////
//                name = extras.getString("name");
//                age = extras.getString("age");
//                needs = extras.getString("bgroup");
//                when = extras.getString("lastRequest");
//                diagnosis = extras.getString("diagnosis");
//                gender = extras.getString("gender");
//                mEmail = extras.getString("mEmail");
//                Toast.makeText(this,gender,Toast.LENGTH_SHORT).show();
//            }
//        } else {
////            Toast.makeText(this, "getting strings from savedInstance",
////                    Toast.LENGTH_SHORT).show();
//            name = (String) savedInstanceState.getSerializable("name");
//            age = (String) savedInstanceState.getSerializable("age");
//            needs = (String) savedInstanceState.getSerializable("bgroup");
//            when = (String) savedInstanceState.getSerializable("lastRequest");
//            diagnosis = (String) savedInstanceState.getSerializable("diagnosis");
//            gender = (String) savedInstanceState.getSerializable("gender");
//            mEmail = (String) savedInstanceState.getSerializable("mEmail");
//
//        }
//
////        TextView mName = (TextView) findViewById(R.id.name);
////        mName.setText("Name: "+name);
////        TextView mAge = (TextView) findViewById(R.id.age);
////        mName.setText("Age: "+age);
////        TextView mWhen = (TextView) findViewById(R.id.blood_g);
////        mWhen.setText("Blood Group: "+needs);
////        TextView mDiagnosis = (TextView) findViewById(R.id.con_num);
////        mDiagnosis.setText("Diagnosis: "+diagnosis);
////        TextView mTransport = (TextView) findViewById(R.id.last);
//        mTransport.setText("Last Request: "+when);
//        ImageView image = (ImageView) findViewById(R.id.image1);
//        if (gender.equals("Female")){
//            image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.girl));
//        }
//        else {
//            image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.boy));
//        }
        //db = FirebaseDatabase.getInstance().getReference().child("patient details");
        FirebaseDatabase.getInstance().getReference().child("patients").child(patient)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Patient data = dataSnapshot.getValue(Patient.class);

                        TextView mName = (TextView) findViewById(R.id.user_profile_name);
                        mName.setText(data.name);
                        pname = data.name;

                        TextView mAge = (TextView) findViewById(R.id.user_profile_age);
                        mAge.setText("Age: " + data.age);

                        TextView mCnumber = (TextView) findViewById(R.id.user_profile_number);
                        mCnumber.setText("Contact Number: " + data.cnumber);
                        num = data.cnumber;

                        TextView mBloodgroup = (TextView) findViewById(R.id.user_profile_bgroup);
                        mBloodgroup.setText("Blood Group: " + data.blood_group);
                        bgroup = data.blood_group;

                        TextView mDiagnosis = (TextView) findViewById(R.id.user_profile_Diagnosis);
                        mDiagnosis.setText("Diagnosis: " + data.diagnosis);
                        diag = data.diagnosis;

//                        Spinner mNeeds = (Spinner) findViewById(R.id.spin1);
//                        mNeeds.getSelectedItem().toString();

//
//                        TextView mLocation = (TextView) findViewById(R.id.request_detail_location);
//                        mLocation.setText(data.location);
//                        TextView mWhen = (TextView) findViewById(R.id.request_detail_when);
//                        String date = DateFormat.getDateInstance().format(new Date(data.date));
//                        mWhen.setText(date);
//                        if (date.equals(DateFormat.getDateInstance().format(new Date()))) {
//                            mWhen.setText("URGENT");
//                            mWhen.setTextColor(0xFFFF0000);
//                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        Button add = (Button) findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loc = location.getText().toString();
                transport_btn = (RadioButton) findViewById(transport_group.getCheckedRadioButtonId());
                quan = quantity.getSelectedItem().toString();

                String transport_text = (String) transport_btn.getText();
                if (transport_text.equals("Available")){
                    transport = true;
                } else if (transport_text.equals("Not Available")){
                    transport = false;
                }
                final Context context = getApplicationContext();
                new_request = db.push();
                String address = loc+", Lahore, Pakistan";
                new GetCoordinates().execute(address.replace(" ", "+"));
                BloodRequest request = new BloodRequest(user.getUid(), pname, bgroup, quan, num, loc, lat, lng, diag, pdate.getTime(), transport);
                new_request.setValue(request);

                dialog = new Dialog(AddRequestOrgActivity.this);
                dialog.setTitle("Add Request");
                dialog.setContentView(R.layout.popup_submit);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final Button add_request = (Button) dialog.findViewById(R.id.button_ok);
                add_request.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddRequestOrgActivity.this,MainActivityOrg.class);
                        intent.putExtra("mEmail", mEmail);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    private class GetCoordinates extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                response = http.getHTTPData(url);
                return response;
            }
            catch (Exception ex)
            {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);
                lat = Double.parseDouble(((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString());
                lng = Double.parseDouble(((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString());
                if (request_added) {
                    new_request.child("latitude").setValue(lat);
                    new_request.child("longitude").setValue(lng);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error locating hospital",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
