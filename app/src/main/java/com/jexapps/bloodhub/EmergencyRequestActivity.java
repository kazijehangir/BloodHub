package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_UI.HttpDataHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class EmergencyRequestActivity extends AppCompatActivity {
// TODO: try to get location automatically
    Dialog dialog;

    AutoCompleteTextView name, location;
    Spinner bloodgroup, quantity, diagnosis;
    EditText number;
    RadioGroup transport_group;
    RadioButton transport_btn;

    String pname, bgroup, quan, diag, num, loc, mEmail;
    double lat = -1, lng = -1;
    Boolean transport;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_request);
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference().child("bloodrequests");
        setTitle("Emergency Request");

        name = (AutoCompleteTextView) findViewById(R.id.name);
        bloodgroup = (Spinner) findViewById(R.id.spin);
        quantity = (Spinner) findViewById(R.id.spin1);
        number = (EditText) findViewById(R.id.contact_num);
        location = (AutoCompleteTextView) findViewById(R.id.loc);
        diagnosis = (Spinner) findViewById(R.id.diagnosis);
        transport_group = (RadioGroup) findViewById(R.id.transport);

        String[] hospitals = getResources().getStringArray(R.array.hospitals);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hospitals);
        location.setAdapter(adapter);

        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pname = name.getText().toString();
                bgroup = bloodgroup.getSelectedItem().toString();
                quan = quantity.getSelectedItem().toString();
                diag = diagnosis.getSelectedItem().toString();
                num = number.getText().toString();
                loc = location.getText().toString();
                transport_btn = (RadioButton) findViewById(transport_group.getCheckedRadioButtonId());
                String transport_text = (String) transport_btn.getText();
                if (transport_text.equals("Available")){
                    transport = true;
                } else if (transport_text.equals("Not Available")){
                    transport = false;
                }
                String address = loc+", Lahore, Pakistan";
                new GetCoordinates().execute(address.replace(" ", "+"));
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
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error locating hospital",Toast.LENGTH_SHORT).show();
            }
            BloodRequest request = new BloodRequest(null, pname, bgroup, quan, num, loc, lat, lng, diag, new Date().getTime(), transport);
            db.push().setValue(request);
            dialog = new Dialog(EmergencyRequestActivity.this);
            dialog.setContentView(R.layout.popup_submit);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final Button submit = (Button) dialog.findViewById(R.id.button_ok);
            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EmergencyRequestActivity.this, SplashActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
