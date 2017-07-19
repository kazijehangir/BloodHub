package com.jexapps.bloodhub;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AddRequestActivity extends AppCompatActivity{
    Dialog dialog;
    private int date, month, year;
    AutoCompleteTextView name;
    Spinner bloodgroup, quantity, diagnosis;
    EditText number, location, when;
    String pname, bgroup, quan, diag, pdate, num, loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        setTitle("Add Request");

        name = (AutoCompleteTextView) findViewById(R.id.name);
        bloodgroup = (Spinner) findViewById(R.id.spin);
        quantity = (Spinner) findViewById(R.id.spin1);
        number = (EditText) findViewById(R.id.contact_num);
        location = (EditText) findViewById(R.id.loc);
        when = (EditText) findViewById(R.id.editText);
        diagnosis = (Spinner) findViewById(R.id.diagnosis);

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
                setDate.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        date = datePicker.getDayOfMonth();
                        month = datePicker.getMonth();
                        year = datePicker.getYear();
                        set.setText(date+"-"+month+"-"+year);
                        dialog.cancel();
                    }
                });
            }
        });

        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = getApplicationContext();
                pname = name.getText().toString();
                bgroup = bloodgroup.getSelectedItem().toString();
                quan = quantity.getSelectedItem().toString();
                diag = diagnosis.getSelectedItem().toString();
                pdate = when.getText().toString();
                num = number.getText().toString();
                loc = location.getText().toString();
                Toast.makeText(context, pname+' '+bgroup+' '+quan+' '+diag+' '+pdate+' '+num+' '+loc,Toast.LENGTH_LONG).show();
//                dialog = new Dialog(AddRequestActivity.this);
//                dialog.setTitle("Submit Request");
//                dialog.setContentView(R.layout.popup_submit);
//                dialog.show();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                final Button request = (Button) dialog.findViewById(R.id.button_ok);
//                request.setOnClickListener(new View.OnClickListener(){
//
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(AddRequestActivity.this,MainActivity.class);
//                        startActivity(intent);
//                    }
//                });
            }
        });
    }
}
