package com.jexapps.bloodhub;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    // add more variables here

    public String username;
    public String email;
    public String account_type; //individual or organization

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String account_type) {
        this.username = username;
        this.email = email;
        this.account_type = account_type;
    }

}
