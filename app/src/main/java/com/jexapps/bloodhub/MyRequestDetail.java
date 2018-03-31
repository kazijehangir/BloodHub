package com.jexapps.bloodhub;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jexapps.bloodhub.m_Model.Donation;
import com.jexapps.bloodhub.m_Model.User;
import com.jexapps.bloodhub.m_UI.RequestListDataAdapter;
import com.jexapps.bloodhub.m_UI.RespondentListDataAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.security.AccessController.getContext;

/**
 * Created by mahnoor on 19/10/2017.
 */

public class MyRequestDetail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    ArrayList<User> users;
    ArrayList<Donation> donations;
    ArrayList<String> keys;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RequestListFragment.OnFragmentInteractionListener mListener;

    DatabaseReference db;
    String name, when, location, request;
//    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_request_details);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Toast.makeText(this, "request: ", Toast.LENGTH_SHORT).show();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                request = null;
            } else {
                request = extras.getString("request");
            }
        }
        db = FirebaseDatabase.getInstance().getReference().child("donations");
        fetchData();

        mRecyclerView = (RecyclerView) findViewById(R.id.respondent_list_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleMarginDecoration(this));

        mAdapter = new RespondentListDataAdapter(users, this);
        mRecyclerView.setAdapter(mAdapter);
        if(request!=null && !request.isEmpty()){
            FirebaseDatabase.getInstance().getReference().child("bloodrequests").child(request).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BloodRequest data = dataSnapshot.getValue(BloodRequest.class);

                    TextView mName = (TextView) findViewById(R.id.name);
                    mName.setText(data.name);
                    TextView mTime = (TextView) findViewById(R.id.time);
                    String date = DateFormat.getDateInstance().format(new Date(data.date));
                    mTime.setText(date);
                    TextView mLocation = (TextView) findViewById(R.id.location);
                    mLocation.setText(data.location);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
//        View rootView = inflater.inflate(R.layout.fragment_my_request_details, container, false);


//        return rootView;

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                request = null;
            } else {
                request = extras.getString("request");
            }
        }
        if(request!=null && !request.isEmpty()){
            FirebaseDatabase.getInstance().getReference().child("bloodrequests").child(request).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BloodRequest data = dataSnapshot.getValue(BloodRequest.class);

                    TextView mName = (TextView) findViewById(R.id.name);
                    mName.setText(data.name);
                    TextView mTime = (TextView) findViewById(R.id.time);
                    String date = DateFormat.getDateInstance().format(new Date(data.date));
                    mTime.setText(date);
                    TextView mLocation = (TextView) findViewById(R.id.location);
                    mLocation.setText(data.location);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        View rootView = inflater.inflate(R.layout.fragment_my_request_details, container, false);
        db = FirebaseDatabase.getInstance().getReference().child("donations");
        fetchData();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.respondent_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleMarginDecoration(this));

        mAdapter = new RespondentListDataAdapter(users, this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public void fetchData(){
        donations = new ArrayList<Donation>();
        keys = new ArrayList<String>();
        users = new ArrayList<User>();
        final DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference().child("users");
//        dbBloodRequests.orderByChild("userid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                requests.clear();
////                keys.clear();
//                for (DataSnapshot child: dataSnapshot.getChildren()) {
//                    requests.add(child.getKey());
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
        Toast.makeText(getApplicationContext(), "request: "+request, Toast.LENGTH_SHORT).show();
        db.orderByChild("requestid").equalTo(request).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Donation donation = child.getValue(Donation.class);
                    Toast.makeText(getApplicationContext(), "donation: "+donation.toString(), Toast.LENGTH_SHORT).show();
                    dbUsers.child(donation.userid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            Toast.makeText(getApplicationContext(),"user: "+ user.username, Toast.LENGTH_SHORT).show();
                            users.add(user);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
