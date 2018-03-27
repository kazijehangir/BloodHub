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
import com.jexapps.bloodhub.m_Model.Appointment;
import com.jexapps.bloodhub.m_UI.MyAppointmentDataAdapter;

import java.util.ArrayList;

public class AppointmentsFragment extends Fragment {
    DatabaseReference db;
    ArrayList<Appointment> appointments;
    ArrayList<String> keys;

    private RecyclerView mRecyclerView;
    private TextView numAppointments;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;

    public AppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_appointments, container, false);
        db = FirebaseDatabase.getInstance().getReference().child("appointments");
        appointments = new ArrayList<Appointment>();
        keys = new ArrayList<String>();
        fetchData();
        numAppointments = (TextView) rootView.findViewById(R.id.num_appointments);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.appointment_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleMarginDecoration(getActivity()));
        mAdapter = new MyAppointmentDataAdapter(appointments, keys);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    //Getting data from database
    public void fetchData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db.orderByChild("userid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appointments.clear();
                keys.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Appointment appointment = child.getValue(Appointment.class);
                    appointments.add(appointment);
                    keys.add(child.getKey());
                }
                numAppointments.setText("Appointments: "+appointments.size());
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAppointmentsFragmentInteraction(uri);
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
        void onAppointmentsFragmentInteraction(Uri uri);
    }
}
