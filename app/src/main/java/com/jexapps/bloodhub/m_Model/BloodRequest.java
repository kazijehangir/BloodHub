package com.jexapps.bloodhub.m_Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BloodRequest {

    public String userid, name, blood_group, quantity, number, location, diagnosis, date;

    public BloodRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(BloodRequest.class)
    }

    public BloodRequest(String userid, String name, String blood_group, String quantity, String number, String location, String diagnosis, String date) {
        this.userid = userid;
        this.name = name;
        this.blood_group = blood_group;
        this.quantity = quantity;
        this.number = number;
        this.location = location;
        this.diagnosis = diagnosis;
        this.date = date;
    }
}
