package com.jexapps.bloodhub.m_UI;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.m_Model.Appointment;
import com.jexapps.bloodhub.m_Model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mahnoor on 12/08/2017.
 */

public class MyAppointmentOrgDataAdapter extends RecyclerView.Adapter<MyAppointmentOrgDataAdapter.ViewHolder> {
    private ArrayList<Appointment> appointments;
    private ArrayList<String> keys;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CharSequence options[] = new CharSequence[] {"Change Timings","Cancel Appointment"};
        public TextView mName, mTransport, mDate, mTime;
        protected CardView cv;
        public Button accept, decline;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTransport = (TextView) itemView.findViewById(R.id.transport);
            mDate = (TextView) itemView.findViewById(R.id.date);
            mTime = (TextView) itemView.findViewById(R.id.time);
            accept = (Button) itemView.findViewById(R.id.button1);
            decline = (Button) itemView.findViewById(R.id.button2);
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    final String id = (String) view.getTag();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                    TextView title = new TextView(view.getContext());
//                    title.setText("Options");
//                    title.setPadding(15, 30, 15, 15);
//                    title.setGravity(Gravity.CENTER);
//                    title.setTypeface(null, Typeface.BOLD);
//                    title.setTextSize(20);
//                    builder.setCustomTitle(title);
//                    builder.setItems(options, new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int which){
//                            if (which == 0){
//
//                            } else if (which == 1){
//                                FirebaseDatabase.getInstance().getReference().child("appointments").child(id).removeValue();
//                            }
//                        }
//                    });
//                    builder.show();
//                    return true;
//                }
//            });
        }
        @Override
        public void onClick(View v) {
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAppointmentOrgDataAdapter(ArrayList<Appointment> app, ArrayList<String> kys) {
        appointments = app;
        keys = kys;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAppointmentOrgDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myappointmentorg_card_view, parent, false);
        return new ViewHolder(v);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyAppointmentOrgDataAdapter.ViewHolder holder, int position) {
        final Appointment appointment = appointments.get(position);
        final String key = keys.get(position);
        holder.cv.setTag(keys.get(position));
        if (appointment.transport){
            holder.mTransport.setText("Yes");
        } else {
            holder.mTransport.setText("No");
        }
        holder.mDate.setText(DateFormat.getDateInstance().format(new Date(appointment.date)));
        holder.mTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(appointment.date)));
        FirebaseDatabase.getInstance().getReference().child("users").child(appointment.userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usr = dataSnapshot.getValue(User.class);
                holder.mName.setText(usr.username);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("appointments").child(key).child("confirmed").setValue(true);
                holder.accept.setVisibility(View.GONE);
                holder.decline.setVisibility(View.GONE);

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return appointments.size();
    }
}
