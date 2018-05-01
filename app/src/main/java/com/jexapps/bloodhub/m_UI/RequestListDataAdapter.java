package com.jexapps.bloodhub.m_UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.RequestDetail;
import com.jexapps.bloodhub.m_Model.BloodRequest;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jehangir Kazi on 23/11/16.
 * This file is supposed to be used as an adapter with the RequestListFragment.java
 * and the request_card_view.xml file.
 */

public class RequestListDataAdapter extends RecyclerView.Adapter<RequestListDataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BloodRequest> requests;
    private ArrayList<String> keys;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CharSequence options[] = new CharSequence[] {"View details","Share"};
        CharSequence share[] = new CharSequence[] {"Facebook","Gmail","Whatsapp"};
        public TextView mName, mLocation, mNeeds, mWhen, mDiagnosis, mTransport;
        public ImageView mImage, mTransportImage;
        protected CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            mName = (TextView) itemView.findViewById(R.id.name_text);
            mLocation = (TextView) itemView.findViewById(R.id.location_text);
            mNeeds = (TextView) itemView.findViewById(R.id.needs_text);
            mWhen = (TextView) itemView.findViewById(R.id.when_text);
            mDiagnosis = (TextView) itemView.findViewById(R.id.diagnosis_text);
            mImage = (ImageView) itemView.findViewById(R.id.request_picture);
            mTransportImage = (ImageView) itemView.findViewById(R.id.transport_image);
            mTransport = (TextView) itemView.findViewById(R.id.transport_text);
            cv.setOnClickListener(this);
            cv.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RequestDetail.class);
            intent.putExtra("request", (String) view.getTag());
            view.getContext().startActivity(intent);
        }

        @Override
        public boolean onLongClick(final View view) {
            final String id = (String) view.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            TextView title = new TextView(view.getContext());
            title.setText("Request options");
            title.setPadding(15, 30, 15, 15);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(null, Typeface.BOLD);
            title.setTextSize(20);
            builder.setCustomTitle(title);
            builder.setItems(options, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    if (which == 0){
                        Intent intent = new Intent(view.getContext(), RequestDetail.class);
                        intent.putExtra("request", id);
                        view.getContext().startActivity(intent);
                    } else if (which == 1){
                        shareDialog(id, view.getContext());
                    }
                }
            });
            builder.show();
            return true;
        }

        private void shareDialog(final String id, final Context context) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            context.startActivity(Intent.createChooser(intent,"Share via"));
            return;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RequestListDataAdapter(ArrayList<BloodRequest> req, ArrayList<String> list, Context context) {
        requests = req;
        mContext = context;
        keys = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RequestListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_card_view, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BloodRequest request = requests.get(position);
        String key = keys.get(position);
        holder.cv.setTag(key);
        holder.mName.setText(request.name);
        holder.mNeeds.setText(request.quantity+" bags of "+request.blood_group);
        holder.mLocation.setText(request.location);
        String date = DateFormat.getDateInstance().format(new Date(request.date));
        holder.mWhen.setText(date);
        if (date.equals(DateFormat.getDateInstance().format(new Date()))) {
            holder.mWhen.setText("URGENT");
            holder.mWhen.setTextColor(0xFFFF0000);
        }
        holder.mDiagnosis.setText(request.diagnosis);

        if (request.transport) {
            holder.mTransport.setText("Available");
            holder.mTransportImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_car));
        } else {
            holder.mTransport.setText("Not Available");
            holder.mTransportImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_no_car));
        }
        try {
            final File localFile = File.createTempFile("images", "jpg");
            FirebaseStorage.getInstance().getReference().child("bloodrequests").child(key)
                    .getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Successfully downloaded data to local file
                    holder.mImage.setImageDrawable(Drawable.createFromPath(localFile.getPath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext,
                            R.drawable.boy));
//                    Toast.makeText(mContext,"Error loading image: " + exception.toString(),
//                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e){
            //IOException: error making temp image
//            Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return requests.size();
    }
}

