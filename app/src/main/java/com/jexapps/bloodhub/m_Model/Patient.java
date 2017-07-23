package com.jexapps.bloodhub.m_Model;

/**
 * Created by aimengaba on 23/07/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Patient {

    public String name;
    public String age;
    public String userid;
    public String blood_group;
    public String cnumber;
    public String diagnosis;
    public String LastRequest;
    // Upload image option

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
        this.LastRequest = null;
    }
}
