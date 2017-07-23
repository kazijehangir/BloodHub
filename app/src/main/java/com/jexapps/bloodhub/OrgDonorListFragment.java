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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_Model.Donor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrgDonorListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrgDonorListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrgDonorListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    DatabaseReference db;
    ArrayList<Donor> donors;
//    "NAME:BGROUP:LOCATION:LASTDONATED:ORIGIN:GENDER:TRANSPORT"
//    private static final String[] dummyDataset = new String[] {
//            "Jamshed:O-:DHA Phase 5:3 Weeks:Patient Attendant:Male",
//            "Aliya:A+:Main Boulevard:1 Week:Blood Drive:Female",
//            "Hamid:AB-:Gulberg:17 Weeks:Walk In:Male",
//            "Saniya:B+:LUMS:5 Weeks:Blood Drive:Female"
//    };
    private String mEmail;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;

    public OrgDonorListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment OrgDonorListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrgDonorListFragment newInstance(String email) {
        OrgDonorListFragment fragment = new OrgDonorListFragment();
        Bundle args = new Bundle();
        args.putString("mEmail", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmail = getArguments().getString("mEmail");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_org_donor_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.org_donor_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleMarginDecoration(getActivity()));

        db = FirebaseDatabase.getInstance().getReference().child("donors");
        // specify an adapter (see also next example)
        mAdapter = new OrgDonorListDataAdapter(fetchData(), getContext());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
    public ArrayList<Donor> fetchData() {
        donors = new ArrayList<Donor>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Donor donor = child.getValue(Donor.class);
                    donors.add(donor);
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return donors;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onOrgDonorListFragmentInteraction(uri);
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
        void onOrgDonorListFragmentInteraction(Uri uri);
    }
}
