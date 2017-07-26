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

public class OrgListDataAdapter extends RecyclerView.Adapter<OrgListDataAdapter.ViewHolder> {
    private ArrayList<User> organizations;
    private ArrayList<String> keys;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        CharSequence options[] = new CharSequence[] {"Change Timings","Cancel Appointment"};
        public TextView mName, mEmail, mNumber, mAddress;
        protected CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            mName = (TextView) itemView.findViewById(R.id.name_text);
            mEmail = (TextView) itemView.findViewById(R.id.email_text);
            mNumber = (TextView) itemView.findViewById(R.id.number_text);
            mAddress = (TextView) itemView.findViewById(R.id.address_text);
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
    public OrgListDataAdapter(ArrayList<User> org, ArrayList<String> kys) {
        organizations = org;
        keys = kys;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrgListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organization_card_view, parent, false);
        return new ViewHolder(v);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User organization = organizations.get(position);
        holder.cv.setTag(keys.get(position));
        holder.mName.setText(organization.username);
        holder.mEmail.setText(organization.email);
        holder.mNumber.setText(organization.number);
        holder.mAddress.setText(organization.address);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return organizations.size();
    }
}
