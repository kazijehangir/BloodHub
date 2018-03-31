package com.jexapps.bloodhub;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_UI.HttpDataHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddRequestActivity extends AppCompatActivity{
    Dialog dialog;
    private int date, month, year;

    AutoCompleteTextView name, location;
    Spinner bloodgroup, quantity, diagnosis;
    EditText number, when;
    RadioGroup transport_group;
    RadioButton transport_btn;
    Uri image_file;
    TextView image;

    String pname, bgroup, quan, diag, num, loc, mEmail;
    Date pdate;
    double lat = -1, lng = -1;
    Boolean transport;

    private static final int GALLERY_INTENT = 2;
    Boolean request_added = false;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db, new_request;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mEmail = user.getEmail();
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference().child("bloodrequests");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("bloodrequests");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        setTitle("Add Request");

        name = (AutoCompleteTextView) findViewById(R.id.name);
        bloodgroup = (Spinner) findViewById(R.id.spin);
        quantity = (Spinner) findViewById(R.id.spin1);
        number = (EditText) findViewById(R.id.contact_num);
        location = (AutoCompleteTextView) findViewById(R.id.loc);
        when = (EditText) findViewById(R.id.editText);
        diagnosis = (Spinner) findViewById(R.id.diagnosis);
        transport_group = (RadioGroup) findViewById(R.id.transport);
        image = (TextView) findViewById(R.id.image_text);

        String[] hospitals = getResources().getStringArray(R.array.organizations_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,hospitals);
        location.setAdapter(adapter);

        final EditText set = (EditText) findViewById(R.id.editText);
        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                dialog = new Dialog(AddRequestActivity.this);
                dialog.setTitle("Set Date and Time");
                dialog.setContentView(R.layout.set_date);
                dialog.show();
                final Button setDate = (Button) dialog.findViewById(R.id.set_date);
                final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
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

        Button image_btn = (Button) findViewById(R.id.upload_image_button);
        image_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 final Context context = getApplicationContext();
                 Intent intent = new Intent(Intent.ACTION_PICK);
                 intent.setType("image/*");
                 startActivityForResult(intent, GALLERY_INTENT);
             }
         });

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
            transport_btn = (RadioButton) findViewById(transport_group.
                    getCheckedRadioButtonId());
            String transport_text = (String) transport_btn.getText();
            if (transport_text.equals("Available")) {
                transport = true;
            } else if (transport_text.equals("Not Available")) {
                transport = false;
            }
            new_request = db.push();
            String address = loc + ", Pakistan";
            new GetCoordinates().execute(address.replace(" ", "+"));
            if (image_file != null) {
                mStorageRef.child(new_request.getKey()).putFile(image_file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                      Toast.makeText(getApplicationContext(),"Image uploaded",
//                          Toast.LENGTH_SHORT).show();
                    String regToken = FirebaseInstanceId.getInstance().getToken();
                    BloodRequest request = new BloodRequest(user.getUid(), pname, bgroup,
                            quan, num, loc, lat, lng, diag, pdate.getTime(), transport,
                            regToken);
                    new_request.setValue(request);
                    request_added = true;
                    dialog = new Dialog(AddRequestActivity.this);
                    dialog.setContentView(R.layout.popup_submit);
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                            Color.TRANSPARENT));

                    final Button submit = (Button) dialog.findViewById(R.id.button_ok);
                    submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AddRequestActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Error uploading image",
                        Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            String regToken = FirebaseInstanceId.getInstance().getToken();
            BloodRequest request = new BloodRequest(user.getUid(), pname, bgroup,
                    quan, num, loc, lat, lng, diag, pdate.getTime(), transport,
                    regToken);
            new_request.setValue(request);
            request_added = true;
            dialog = new Dialog(AddRequestActivity.this);
            dialog.setContentView(R.layout.popup_submit);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                    Color.TRANSPARENT));

            final Button submit = (Button) dialog.findViewById(R.id.button_ok);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddRequestActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                }
            });
            }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            image_file = data.getData();
            image.setText("Image added!");
        }
    }

    private class GetCoordinates extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... address) {
            String response;
            try {
//                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format(
                        "https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                response = http.getHTTPData(url);
                return response;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error in map search",Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);
                lat = Double.parseDouble(((JSONArray)jsonObject.get("results"))
                        .getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                        .get("lat").toString());
                lng = Double.parseDouble(((JSONArray)jsonObject.get("results"))
                        .getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                        .get("lng").toString());
                if (request_added) {
                    new_request.child("latitude").setValue(lat + ((Math.random() - 0.5) / 4000));
                    new_request.child("longitude").setValue(lng + ((Math.random() - 0.5) / 4000));
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error locating hospital",Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
