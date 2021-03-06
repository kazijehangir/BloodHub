package com.jexapps.bloodhub.m_UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jexapps.bloodhub.MyRequestDetail;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.m_Model.BloodRequest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmergencyRequestDataAdapter extends RecyclerView.Adapter<EmergencyRequestDataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BloodRequest> requests;
    private ArrayList<String> keys;
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mName, mLocation, mWhen;
        public ImageButton delete, edit;
        protected CardView cv;
//        public ImageView mImage, mTransportImage;
        public ViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mLocation = (TextView) itemView.findViewById(R.id.location);
            mWhen = (TextView) itemView.findViewById(R.id.time);
            delete = (ImageButton) itemView.findViewById(R.id.delete);
            edit = (ImageButton) itemView.findViewById(R.id.edit);
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//            final Query request = ref.child("bloodrequests").child((String) itemView.getTag());
            delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ref.child("bloodrequests").child((String) itemView.getTag()).removeValue();
                }
            });
            cv.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), MyRequestDetail.class);
            intent.putExtra("request", (String) view.getTag());
            view.getContext().startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EmergencyRequestDataAdapter(ArrayList<BloodRequest> req, ArrayList<String> list, Context context) {
        requests = req;
        mContext = context;
        keys = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EmergencyRequestDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emergency_request_card_view, parent, false);
        return new ViewHolder(v);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EmergencyRequestDataAdapter.ViewHolder holder, int position) {
        BloodRequest request = (BloodRequest) requests.get(position);
        String key = keys.get(position);
        holder.cv.setTag(key);
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
