package com.jexapps.bloodhub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_UI.HttpDataHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class AddEmergencyRequestActivity extends AppCompatActivity {
    Dialog dialog;

    AutoCompleteTextView name, location;
    Spinner bloodgroup, quantity, diagnosis;
    EditText number;
    RadioGroup transport_group;
    RadioButton transport_btn;

    String pname, bgroup, quan, diag, num, loc, mEmail;
    double lat = -1, lng = -1;
    Boolean transport;
    String newRequestKey;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

//    String requestConfirmationTextStr = "Dummy test message.";
//    String confirmationLink = "google.com";

    DatabaseReference db;

    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID =
            "AC8cafc06aa846c148f46ceb38abb713cb";
//    public static final String AUTH_TOKEN =
//            "a1be8c230a1ede6964544fd63bb7537c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emergency_request);
        sharedPref = this.getSharedPreferences("EmergencyRequests", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference().child("bloodrequests");
        setTitle("Emergency Request");
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        name = (AutoCompleteTextView) findViewById(R.id.name);
        bloodgroup = (Spinner) findViewById(R.id.spin);
        quantity = (Spinner) findViewById(R.id.spin1);
        number = (EditText) findViewById(R.id.contact_num);
        location = (AutoCompleteTextView) findViewById(R.id.loc);
        diagnosis = (Spinner) findViewById(R.id.diagnosis);
        transport_group = (RadioGroup) findViewById(R.id.transport);

        String[] hospitals = getResources().getStringArray(R.array.organizations_array);
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
                if (!((num.startsWith("+923") && num.length() == 13) ||
                        (num.startsWith("03") && num.length() == 11))) {
                    number.setError(
                            "Please enter a valid number in the +923XX XXXXXXX or 03XX XXXXXXX Format");
                    return;
                }
                if (num.startsWith("03")) {
                    num = "+923" + num.substring(2);
                }

                loc = location.getText().toString();
                transport_btn = (RadioButton) findViewById(transport_group.getCheckedRadioButtonId());
                String transport_text = (String) transport_btn.getText();
                if (transport_text.equals("Available")){
                    transport = true;
                } else if (transport_text.equals("Not Available")){
                    transport = false;
                }
                String address = loc + ", Pakistan";
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

            String regToken = FirebaseInstanceId.getInstance().getToken();
            BloodRequest request = new BloodRequest(null, pname, bgroup, quan, num, loc,
                    lat + ((Math.random() - 0.5) / 4000),
                    lng + ((Math.random() - 0.5) / 4000),
                    diag, new Date().getTime(), transport, regToken);

            DatabaseReference newRequest = db.push();
            newRequestKey = newRequest.getKey();
            newRequest.setValue(request);

            String existingKeys = sharedPref.getString("keys", "");

            String newKeys;
            if (existingKeys != "") {
                newKeys = existingKeys + ":" + newRequestKey;
            } else {
                newKeys = newRequestKey;
            }
            editor.putString("keys", newKeys);
            editor.commit();

            dialog = new Dialog(AddEmergencyRequestActivity.this);
            dialog.setContentView(R.layout.popup_submit);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final Button submit = (Button) dialog.findViewById(R.id.button_ok);
            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddEmergencyRequestActivity.this, SplashActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
