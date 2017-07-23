package com.jexapps.bloodhub;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_Model.Donation;
import com.jexapps.bloodhub.m_UI.MyDonationDataAdapter;
import com.jexapps.bloodhub.m_UI.MyRequestDataAdapter;

import java.util.ArrayList;

public class DonationsFragment extends Fragment {
    DatabaseReference db;
    ArrayList<BloodRequest> donations;

    private RecyclerView mRecyclerView;
    private TextView numDonations;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;

    public DonationsFragment() {
        // Required empty public constructor
    }

//    public static DonationsFragment newInstance(String param1, String param2) {
//        DonationsFragment fragment = new DonationsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_donations, container, false);
        numDonations = (TextView) rootView.findViewById(R.id.num_donations);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.donation_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleMarginDecoration(getActivity()));
        db = FirebaseDatabase.getInstance().getReference().child("donations");
        mAdapter = new MyDonationDataAdapter(fetchData());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    //Getting data from database
    public ArrayList<BloodRequest> fetchData() {
        donations = new ArrayList<BloodRequest>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db.orderByChild("userid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Donation donation = child.getValue(Donation.class);
                    FirebaseDatabase.getInstance().getReference().child("bloodrequests").child(donation.requestid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            BloodRequest request = dataSnapshot.getValue(BloodRequest.class);
                            donations.add(request);
                            numDonations.setText("LIVES SAVED: "+donations.size());
                            mAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return donations;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDonationsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDonationsFragmentInteraction(Uri uri);
    }
}
