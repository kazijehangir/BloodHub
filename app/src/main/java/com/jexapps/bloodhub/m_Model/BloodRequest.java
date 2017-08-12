package com.jexapps.bloodhub.m_Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

@IgnoreExtraProperties
public class BloodRequest {

    public String userid, name, blood_group, quantity, number, location, diagnosis, regToken;
    public double latitude, longitude;
    public Boolean completed, transport;
    public long date, created_at;

    public BloodRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(BloodRequest.class)
    }

    public BloodRequest(String userid, String name, String blood_group, String quantity, String number, String location, double latitude, double longitude, String diagnosis, long date, Boolean transport, String regToken) {
        this.userid = userid;
        this.name = name;
        this.blood_group = blood_group;
        this.quantity = quantity;
        this.number = number;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.diagnosis = diagnosis;
        this.date = date;
        this.transport = transport;
        this.regToken = regToken;
        this.completed = false;
        this.created_at = Calendar.getInstance().getTime().getTime();
    }
}
