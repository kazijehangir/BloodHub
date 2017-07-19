package com.jexapps.bloodhub;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    // add more variables here

    public String username;
    public String email;
    public String account_type; //individual or organization
    public String blood_group;
    public String number;
    public String address;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String blood_group) {
        this.username = username;
        this.email = email;
        this.account_type = "individual";
        this.blood_group = blood_group;
    }

    public User(String username, String email, String number, String address) {
        this.username = username;
        this.email = email;
        this.account_type = "organization";
        this.number = number;
        this.address = address;
    }

}