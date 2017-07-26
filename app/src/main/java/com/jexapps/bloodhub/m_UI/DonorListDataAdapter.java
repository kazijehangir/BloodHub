package com.jexapps.bloodhub.m_UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jexapps.bloodhub.OrgDonorDetail;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.m_Model.Donor;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Jehangir Kazi on 23/11/16.
 * This file is supposed to be used as an adapter with the RequestListFragment.java
 * and the request_card_view.xml file.
 */

public class DonorListDataAdapter extends RecyclerView.Adapter<DonorListDataAdapter.ViewHolder> {
//    private String[] mDataset;
    private final Context mContext;
    private ArrayList<Donor> donors;
    private ArrayList<String> keys;
    private static String mEmail;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        CharSequence options[] = new CharSequence[] {"Delete Donor"};
        public TextView mName, mLocation, mBgroup, mLastDonated, mOrigin;
        public ImageView mImage;
        protected CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            mName = (TextView) itemView.findViewById(R.id.name_text);
            mLocation = (TextView) itemView.findViewById(R.id.location_text);
            mBgroup = (TextView) itemView.findViewById(R.id.bgroup_text);
            mLastDonated = (TextView) itemView.findViewById(R.id.last_donated_text);
            mOrigin = (TextView) itemView.findViewById(R.id.donor_origin_text);
            mImage = (ImageView) itemView.findViewById(R.id.request_picture);
            itemView.findViewById(R.id.card_view).setOnClickListener(this);
            itemView.findViewById(R.id.card_view).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final Object text = view.getTag();
                    Toast toast = Toast.makeText(view.getContext(), text.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            final Query donor = ref.child("donors").child(text.toString());

                            donor.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot donor: dataSnapshot.getChildren()) {
                                        donor.getRef().removeValue();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, "onCancelled", databaseError.toException()
                                    );
                                }
                            });
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), OrgDonorDetail.class);
            TextView mName = (TextView) itemView.findViewById(R.id.name_text);
            intent.putExtra("name", mName.getText());
            TextView mBgroup = (TextView) itemView.findViewById(R.id.bgroup_text);
            intent.putExtra("bgroup", mBgroup.getText());
            TextView mLocation = (TextView) itemView.findViewById(R.id.location_text);
            intent.putExtra("location", mLocation.getText());
            TextView mLastDonated = (TextView) itemView.findViewById(R.id.last_donated_text);
            intent.putExtra("lastDonated", mLastDonated.getText());
            TextView mOrigin = (TextView) itemView.findViewById(R.id.donor_origin_text);
            intent.putExtra("origin", mOrigin.getText());
            intent.putExtra("mEmail", mEmail);
            v.getContext().startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DonorListDataAdapter(ArrayList<Donor> don, ArrayList<String> list, Context context) {
        donors = don;
        mContext = context;
        keys = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DonorListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.org_donor_card_view, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Donor donor = (Donor) donors.get(position);
        holder.cv.setTag(keys.get(position));
//        String[] strings = mDataset[position].split(":");
        holder.mName.setText(donor.name);
        holder.mBgroup.setText(donor.blood_group);
        holder.mLocation.setText(donor.location);
        holder.mLastDonated.setText(donor.lastDonated);
        holder.mOrigin.setText(donor.donorOrigin);
        holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.boy));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return donors.size();
    }
}

