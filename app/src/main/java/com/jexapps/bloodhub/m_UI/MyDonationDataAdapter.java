package com.jexapps.bloodhub.m_UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.RequestDetail;
import com.jexapps.bloodhub.m_Model.BloodRequest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyDonationDataAdapter extends RecyclerView.Adapter<MyDonationDataAdapter.ViewHolder> {
    private ArrayList<BloodRequest> requests;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mName, mLocation, mWhen;
        //        public ImageView mImage, mTransportImage;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mLocation = (TextView) itemView.findViewById(R.id.location);
            mWhen = (TextView) itemView.findViewById(R.id.time);
        }
        @Override
        public void onClick(View v) {
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyDonationDataAdapter(ArrayList<BloodRequest> req) {
        requests = req;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyDonationDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myrequest_card_view, parent, false);
        return new ViewHolder(v);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyDonationDataAdapter.ViewHolder holder, int position) {
        try{
            BloodRequest request = (BloodRequest) requests.get(position);
            holder.mName.setText(request.name);
            holder.mLocation.setText(request.location);
            holder.mWhen.setText(DateFormat.getDateInstance().format(new Date(request.date)));
        } catch (Exception e){

        };

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d("PLISS",""+requests.size());
        return requests.size();
    }
}
