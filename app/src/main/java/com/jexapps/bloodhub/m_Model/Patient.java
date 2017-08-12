package com.jexapps.bloodhub.m_Model;

/**
 * Created by aimengaba on 23/07/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

@IgnoreExtraProperties
public class Patient {

    public String name, age, userid, blood_group, cnumber, diagnosis, LastRequest;
    public long created_at;

    public Patient() {
        // Default constructor required for calls to DataSnapshot.getValue(Patient.class)
    }

    public Patient(String userid, String name, String age, String blood_group, String cnumber, String diagnosis){
        this.userid = userid;
        this.name = name;
        this.age = age;
        this.blood_group = blood_group;
        this.cnumber = cnumber;
        this.diagnosis = diagnosis;
        this.created_at = Calendar.getInstance().getTime().getTime();
        this.LastRequest = null;
    }
}
