package com.jexapps.bloodhub.m_Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

/**
 * Created by hp on 7/23/2017.
 */
@IgnoreExtraProperties
public class Donation {
    public String userid, requestid, status;
    public Boolean userConfirmation, requestConfirmation;
    public long created_at;

    public Donation() {
        // Default constructor required for calls to DataSnapshot.getValue(BloodRequest.class)
    }

    public Donation(String userid, String requestid) {
        this.userid = userid;
        this.requestid = requestid;
        this.status = "Pending";
        this.userConfirmation = false;
        this.requestConfirmation = false;
        this.created_at = Calendar.getInstance().getTime().getTime();
    }
}
