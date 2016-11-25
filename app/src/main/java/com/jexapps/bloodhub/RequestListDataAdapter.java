package com.jexapps.bloodhub;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jehangir on 23/11/16.
 */

public class RequestListDataAdapter extends RecyclerView.Adapter<RequestListDataAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName, mLocation, mNeeds, mWhen, mDiagnosis;
        public ViewHolder(TextView name, TextView location, TextView needs, TextView when, TextView diagnosis) {
            super(name);
            mName = name;
            mLocation = location;
            mNeeds = needs;
            mWhen = when;
            mDiagnosis = diagnosis;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RequestListDataAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RequestListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        TextView tvName = (TextView) v.findViewById(R.id.name_text);
        if (tvName.getParent() != null) {
            ((ViewGroup)tvName.getParent()).removeView(tvName);
        }
        TextView tvNeeds = (TextView) v.findViewById(R.id.needs_text);
        if (tvNeeds.getParent() != null) {
            ((ViewGroup)tvNeeds.getParent()).removeView(tvNeeds);
        }
        TextView tvLocation = (TextView) v.findViewById(R.id.location_text);
        if (tvLocation.getParent() != null) {
            ((ViewGroup)tvLocation.getParent()).removeView(tvLocation);
        }
        TextView tvWhen = (TextView) v.findViewById(R.id.when_text);
        if (tvWhen.getParent() != null) {
            ((ViewGroup)tvWhen.getParent()).removeView(tvWhen);
        }
        TextView tvDiagnosis = (TextView) v.findViewById(R.id.diagnosis_text);
        if (tvDiagnosis.getParent() != null) {
            ((ViewGroup)tvDiagnosis.getParent()).removeView(tvDiagnosis);
        }
        ViewHolder vh = new ViewHolder(tvName, tvNeeds, tvLocation, tvWhen, tvDiagnosis);
        return vh;
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
        holder.mDiagnosis.setText(strings[4]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

