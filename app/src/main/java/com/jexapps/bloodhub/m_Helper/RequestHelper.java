package com.jexapps.bloodhub.m_Helper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.jexapps.bloodhub.m_Model.BloodRequest;

import java.util.ArrayList;

public class RequestHelper {
    DatabaseReference db;
    Boolean saved;
    ArrayList<BloodRequest> requests=new ArrayList<>();
    /*
 PASS DATABASE REFRENCE
  */
    public RequestHelper(DatabaseReference db) {
        this.db = db;
    }
    //WRITE IF NOT NULL
    public Boolean save(BloodRequest request)
    {
        if(request == null)
        {
            saved = false;
        } else {
            try {
                db.child("bloodrequests").push().setValue(request);
                saved = true;
            } catch (DatabaseException e) {
                saved = false;
            }
        }
        return saved;
    }
    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    private void fetchData(DataSnapshot dataSnapshot)
    {
        requests.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            BloodRequest request = ds.getValue(BloodRequest.class);
            requests.add(request);
        }
    }
    //RETRIEVE
    public ArrayList<BloodRequest> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return requests;
    }
}
