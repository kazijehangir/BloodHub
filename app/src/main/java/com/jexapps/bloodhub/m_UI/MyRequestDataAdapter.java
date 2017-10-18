package com.jexapps.bloodhub.m_UI;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jexapps.bloodhub.MyRequestDetail;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.RequestDetail;
import com.jexapps.bloodhub.m_Model.BloodRequest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyRequestDataAdapter extends RecyclerView.Adapter<MyRequestDataAdapter.ViewHolder> {
    private ArrayList<BloodRequest> requests;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mName, mLocation, mWhen;
        protected CardView cv;
//        public ImageView mImage, mTransportImage;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            mName = (TextView) itemView.findViewById(R.id.name);
            mLocation = (TextView) itemView.findViewById(R.id.location);
            mWhen = (TextView) itemView.findViewById(R.id.time);
            cv.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MyRequestDetail.class);
            intent.putExtra("request", (String) view.getTag());
            view.getContext().startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRequestDataAdapter(ArrayList<BloodRequest> req) {
        requests = req;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRequestDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myrequest_card_view, parent, false);
        return new ViewHolder(v);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyRequestDataAdapter.ViewHolder holder, int position) {
        BloodRequest request = (BloodRequest) requests.get(position);
        holder.mName.setText(request.name);
        holder.mLocation.setText(request.location);
        holder.mWhen.setText(DateFormat.getDateInstance().format(new Date(request.date)));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return requests.size();
    }
}
