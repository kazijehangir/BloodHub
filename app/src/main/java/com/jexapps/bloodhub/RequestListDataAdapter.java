package com.jexapps.bloodhub;

import android.content.Context;
import android.graphics.Rect;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.jexapps.bloodhub.R.drawable.boy;

/**
 * Created by Jehangir Kazi on 23/11/16.
 * This file is supposed to be used as an adapter with the RequestListFragment.java
 * and the request_card_view.xml file.
 */

public class RequestListDataAdapter extends RecyclerView.Adapter<RequestListDataAdapter.ViewHolder> {
    private String[] mDataset;
    private  Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName, mLocation, mNeeds, mWhen, mDiagnosis, mTransport;
        public ImageView mImage, mTransportImage;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name_text);
            mLocation = (TextView) itemView.findViewById(R.id.location_text);
            mNeeds = (TextView) itemView.findViewById(R.id.needs_text);
            mWhen = (TextView) itemView.findViewById(R.id.when_text);
            mDiagnosis = (TextView) itemView.findViewById(R.id.diagnosis_text);
            mImage = (ImageView) itemView.findViewById(R.id.request_picture);
            mTransportImage = (ImageView) itemView.findViewById(R.id.transport_image);
            mTransport = (TextView) itemView.findViewById(R.id.transport_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RequestListDataAdapter(String[] myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RequestListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_card_view, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String[] strings = mDataset[position].split(":");
        holder.mName.setText(strings[0]);
        holder.mNeeds.setText(strings[1]);
        holder.mLocation.setText(strings[2]);
        holder.mWhen.setText(strings[3]);
        if (strings[3].equals("URGENT")) {
            holder.mWhen.setTextColor(0xFFFF0000);
        }
        holder.mDiagnosis.setText(strings[4]);
//        TODO: FIX THESE IMAGE ASSIGNMENTS
        if (strings[5].equals("Male")) {
            holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.boy));
        } else if (strings[5].equals("Female")) {
            holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.girl));
        }
        if (strings[6].equals("Yes")) {
            holder.mTransport.setText("Available");
            holder.mTransportImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_car));
        } else if (strings[6].equals("No")) {
            holder.mTransport.setText("Not Available");
            holder.mTransportImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_no_car));
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

