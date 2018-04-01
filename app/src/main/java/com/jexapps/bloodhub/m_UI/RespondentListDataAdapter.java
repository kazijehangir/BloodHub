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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jexapps.bloodhub.MyRequestDetail;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.RequestDetail;
import com.jexapps.bloodhub.m_Model.BloodRequest;
import com.jexapps.bloodhub.m_Model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RespondentListDataAdapter extends RecyclerView.Adapter<RespondentListDataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<User> users;
//    private ArrayList<String> keys;
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mName, mEmail, mContact, mAge;
        protected CardView cv;
        //        public ImageView mImage, mTransportImage;
        public ViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mEmail = (TextView) itemView.findViewById(R.id.email);
            mContact = (TextView) itemView.findViewById(R.id.contact_num);
            mAge = (TextView) itemView.findViewById(R.id.age);
//            edit = (ImageButton) itemView.findViewById(R.id.edit);
//            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
////            final Query request = ref.child("bloodrequests").child((String) itemView.getTag());
//            delete.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    ref.child("bloodrequests").child((String) itemView.getTag()).removeValue();
//                }
//            });
//            cv.setOnClickListener(this);
//            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), MyRequestDetail.class);
            intent.putExtra("request", (String) view.getTag());
            view.getContext().startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RespondentListDataAdapter(ArrayList<User> user, Context context) {
        users = user;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RespondentListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.respodents_card_view, parent, false);
        return new ViewHolder(v);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RespondentListDataAdapter.ViewHolder holder, int position) {
        User user = (User) users.get(position);
//        String key = keys.get(position);
//        holder.cv.setTag(key);
        holder.mName.setText(user.username);
        holder.mEmail.setText(user.email);
        holder.mAge.setText("0"); //TODO: add age when registering user and set text here
        holder.mContact.setText("0"); //TODO: add contact when registering and set text here
//        holder.mWhen.setText(DateFormat.getDateInstance().format(new Date(request.date)));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }
}
