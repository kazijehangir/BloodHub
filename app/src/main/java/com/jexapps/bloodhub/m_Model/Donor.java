package com.jexapps.bloodhub.m_Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Donor {

    public String userid, name, blood_group, number, location, lastDonated, donorOrigin, age;

    public Donor() {
        // Default constructor required for calls to DataSnapshot.getValue(BloodRequest.class)
    }

    public Donor(String userid, String name, String blood_group, String number, String location, String lastDonated, String donorOrigin, String age) {
        this.userid = userid;
        this.name = name;
        this.blood_group = blood_group;
        this.number = number;
        this.location = location;
        this.lastDonated = lastDonated;
        this.donorOrigin = donorOrigin;
        this.age = age;
    }
}
