package com.jexapps.bloodhub.m_Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by hp on 7/23/2017.
 */
@IgnoreExtraProperties
public class Donation {
    public String userid, requestid;

    public Donation() {
        // Default constructor required for calls to DataSnapshot.getValue(BloodRequest.class)
    }

    public Donation(String userid, String requestid) {
        this.userid = userid;
        this.requestid = requestid;
    }
}
