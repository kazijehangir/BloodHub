package com.jexapps.bloodhub;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jehangir Kazi on 23/11/16.
 * This file is supposed to be used as an adapter with the RequestListFragment.java
 * and the request_card_view.xml file.
 */

public class OrgPatientListDataAdapter extends RecyclerView.Adapter<OrgPatientListDataAdapter.ViewHolder> {
    private String[] mDataset;
    private final Context mContext;
    private static String mEmail, gender;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mName, mLastRequest, mBgroup, mStatus, mDiagnosis;
        public ImageView mImage;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name_text);
            mLastRequest = (TextView) itemView.findViewById(R.id.last_request_text);
            mBgroup = (TextView) itemView.findViewById(R.id.bgroup_text);
            mDiagnosis = (TextView) itemView.findViewById(R.id.diagnosis_text);
            mImage = (ImageView) itemView.findViewById(R.id.request_picture);
            itemView.findViewById(R.id.card_view).setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AddRequestOrgActivity.class);
            TextView mName = (TextView) itemView.findViewById(R.id.name_text);
            intent.putExtra("name", mName.getText());
            TextView mDiagnosis = (TextView) itemView.findViewById(R.id.diagnosis_text);
            intent.putExtra("diagnosis", mDiagnosis.getText());
            TextView mLastRequest = (TextView) itemView.findViewById(R.id.last_request_text);
            intent.putExtra("lastRequest", mLastRequest.getText());
            TextView mBgroup = (TextView) itemView.findViewById(R.id.bgroup_text);
            intent.putExtra("bgroup", mBgroup.getText());
            intent.putExtra("gender", "Male");
            intent.putExtra("mEmail", mEmail);
            v.getContext().startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrgPatientListDataAdapter(String[] myDataset, Context context, String email) {
        mDataset = myDataset;
        mContext = context;
        mEmail = email;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrgPatientListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.org_patient_card_view, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String[] strings = mDataset[position].split(":");
        holder.mName.setText(strings[0]);
        holder.mBgroup.setText(strings[1]);
        holder.mDiagnosis.setText(strings[2]);
        holder.mLastRequest.setText(strings[3]);
        //holder.mStatus.setText(strings[4]);
        if (strings[5].equals("Male")) {
            holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.boy));
        } else if (strings[5].equals("Female")) {
            holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.girl));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

