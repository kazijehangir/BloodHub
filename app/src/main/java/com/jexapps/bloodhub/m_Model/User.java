package com.jexapps.bloodhub.m_Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

@IgnoreExtraProperties
public class User {
    // add more variables here

    public String username, email, account_type, blood_group, number, address;
    public long created_at;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String blood_group) {
        this.username = username;
        this.email = email;
        this.account_type = "individual";
        this.blood_group = blood_group;
        this.created_at = Calendar.getInstance().getTime().getTime();
    }

    public User(String username, String email, String number, String address) {
        this.username = username;
        this.email = email;
        this.account_type = "organization";
        this.number = number;
        this.address = address;
        this.created_at = Calendar.getInstance().getTime().getTime();
    }

}
