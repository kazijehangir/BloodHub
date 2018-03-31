package com.jexapps.bloodhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jexapps.bloodhub.m_Firebase.FirebaseInstanceIDService;
import com.jexapps.bloodhub.m_Model.User;

import java.util.Arrays;

public class SettingsFragment extends Fragment {
    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private int bgroup_id;
    SwitchCompat bgroup_switch, urgent_switch, drive_switch;
    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        bgroup_switch = (SwitchCompat) rootView.findViewById(R.id.bgroup_switch);
        urgent_switch = (SwitchCompat) rootView.findViewById(R.id.urgent_switch);
        drive_switch = (SwitchCompat) rootView.findViewById(R.id.drive_switch);
        sharedPref = getContext().getSharedPreferences(getString(R.string.pref_file_settings), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Log.d("Token", FirebaseInstanceId.getInstance().getToken());
        getValues();
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User usr = dataSnapshot.getValue(User.class);
                        bgroup_id = Arrays.asList(getResources().getStringArray(R.array.blood_groups)).indexOf(usr.blood_group);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        bgroup_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean(getString(R.string.key_group_request), b);
                if (b){
                    FirebaseMessaging.getInstance().subscribeToTopic("Request_"+bgroup_id);
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Request_"+bgroup_id);
                }
                editor.commit();
            }
        });
        urgent_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean(getString(R.string.key_urgent_request), b);
                if (b){
                    FirebaseMessaging.getInstance().subscribeToTopic("URGENT");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("URGENT");
                }
                editor.commit();
            }
        });
        drive_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean(getString(R.string.key_drive), b);
                editor.commit();
            }
        });
        return rootView;
    }

    private void getValues(){
        bgroup_switch.setChecked(sharedPref.getBoolean(getString(R.string.key_group_request), true));
        urgent_switch.setChecked(sharedPref.getBoolean(getString(R.string.key_urgent_request), true));
        drive_switch.setChecked(sharedPref.getBoolean(getString(R.string.key_drive), true));
    };

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSettingsFragmentInteraction(uri);
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
        void onSettingsFragmentInteraction(Uri uri);
    }
}
