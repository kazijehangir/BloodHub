package com.jexapps.bloodhub;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_UI.MyRequestDataAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ManageEmergencyRequestsActivity extends AppCompatActivity {
    DatabaseReference db;
    ArrayList<BloodRequest> requests;
    ArrayList<String> keys;

    private RecyclerView mRecyclerView;
    private TextView numRequests;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_emergency_requests);
        setTitle("Your Previous Requests");
        numRequests = (TextView) findViewById(R.id.num_requests);
        mRecyclerView = (RecyclerView) findViewById(R.id.request_list_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleMarginDecoration(this));
        db = FirebaseDatabase.getInstance().getReference().child("bloodrequests");
        fetchData();
        mAdapter = new MyRequestDataAdapter(requests,keys, this);
        mRecyclerView.setAdapter(mAdapter);

    }
    //Getting data from database
    public void fetchData() {
        requests = new ArrayList<BloodRequest>();
        keys = new ArrayList<String>();

        SharedPreferences sharedPref = this.getSharedPreferences(
                "EmergencyRequests", this.MODE_PRIVATE);
        String existingKeys = sharedPref.getString("keys", "");

        keys = new ArrayList<>(Arrays.asList(existingKeys.split(":")));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if (keys.contains(child.getKey())) {
                        BloodRequest request = child.getValue(BloodRequest.class);
                        requests.add(request);
                    }
                }
                numRequests.setText("Total Requests: "+requests.size());
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return;
    }
}
