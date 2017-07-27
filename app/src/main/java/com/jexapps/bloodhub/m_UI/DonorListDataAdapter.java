package com.jexapps.bloodhub.m_UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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

public class DonorListDataAdapter extends RecyclerView.Adapter<DonorListDataAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<Donor> donors;
    private ArrayList<String> keys;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            cv.setOnClickListener(this);
            cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final String id = (String) view.getTag();
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    TextView title = new TextView(view.getContext());
                    title.setText("Options");
                    title.setPadding(15, 30, 15, 15);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setTextSize(20);
                    builder.setCustomTitle(title);
                    builder.setItems(options, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            // add if-statement for more than one options
                            FirebaseDatabase.getInstance().getReference().child("donors").child(id).removeValue();
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
            intent.putExtra("donor", (String) v.getTag());
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
        Donor donor = donors.get(position);
        holder.cv.setTag(keys.get(position));
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

