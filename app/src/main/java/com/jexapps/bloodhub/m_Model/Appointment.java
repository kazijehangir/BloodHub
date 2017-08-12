package com.jexapps.bloodhub.m_Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@IgnoreExtraProperties
public class Appointment {

    public String userid, orgid;
    public long date, created_at;
    public Boolean transport, confirmed;

    public Appointment() {
        // Default constructor required for calls to DataSnapshot.getValue(BloodRequest.class)
    }

    public Appointment(String userid, String orgid, String date, String time, Boolean transport) {
        this.userid = userid;
        this.orgid = orgid;
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");
        try {
            this.date = df.parse(date+" "+time).getTime();
        } catch (Exception e){
        }
        this.transport = transport;
        this.created_at = Calendar.getInstance().getTime().getTime();
        this.confirmed = false;
    }
}
