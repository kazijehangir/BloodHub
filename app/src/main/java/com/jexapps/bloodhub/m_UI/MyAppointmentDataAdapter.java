package com.jexapps.bloodhub.m_UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.m_Model.Appointment;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_Model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAppointmentDataAdapter extends RecyclerView.Adapter<MyAppointmentDataAdapter.ViewHolder> {
    private ArrayList<Appointment> appointments;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mName, mTransport, mDate, mTime;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTransport = (TextView) itemView.findViewById(R.id.transport);
            mDate = (TextView) itemView.findViewById(R.id.date);
            mTime = (TextView) itemView.findViewById(R.id.time);
        }
        @Override
        public void onClick(View v) {
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAppointmentDataAdapter(ArrayList<Appointment> app) {
        appointments = app;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAppointmentDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myappointment_card_view, parent, false);
        return new ViewHolder(v);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Appointment appointment = (Appointment) appointments.get(position);
        if (appointment.transport){
            holder.mTransport.setText("Yes");
        } else {
            holder.mTransport.setText("No");
        }
        holder.mDate.setText(DateFormat.getDateInstance().format(new Date(appointment.date)));
        holder.mTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(appointment.date)));
        FirebaseDatabase.getInstance().getReference().child("users").child(appointment.orgid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usr = dataSnapshot.getValue(User.class);
                holder.mName.setText(usr.username);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return appointments.size();
    }
}
