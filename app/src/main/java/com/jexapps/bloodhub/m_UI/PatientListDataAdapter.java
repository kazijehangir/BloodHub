package com.jexapps.bloodhub.m_UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import com.jexapps.bloodhub.AddRequestOrgActivity;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.m_Model.Patient;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

//import com.jexapps.bloodhub.PatientDetail;

/**
 * Created by aimengaba on 23/07/2017.
 */

public class PatientListDataAdapter extends RecyclerView.Adapter<PatientListDataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Patient> patients;
    private ArrayList<String> keys;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CharSequence options[] = new CharSequence[] {"Delete Patient"};
        public TextView mName, mAge, mBloodgroup, mCnumber, mDiagnosis, mLastRequest;
        public ImageView mImage;
        protected CardView cv;
        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            mName = (TextView) itemView.findViewById(R.id.name_text);
            mAge = (TextView) itemView.findViewById(R.id.age_text);
            mBloodgroup = (TextView) itemView.findViewById(R.id.bgroup_text);
            mCnumber = (TextView) itemView.findViewById(R.id.number_text);
            mDiagnosis = (TextView) itemView.findViewById(R.id.diagnosis_text);
            mLastRequest = (TextView) itemView.findViewById(R.id.last_request_text);
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
                            FirebaseDatabase.getInstance().getReference().child("patients").child(id).removeValue();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AddRequestOrgActivity.class);
            intent.putExtra("patient", (String) v.getTag());
            v.getContext().startActivity(intent);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PatientListDataAdapter(ArrayList<Patient> pat, ArrayList<String> list, Context context) {
        patients = pat;
        mContext = context;
        keys = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PatientListDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.org_patient_card_view, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PatientListDataAdapter.ViewHolder holder, int position) {
        Patient pat = (Patient) patients.get(position);
        holder.cv.setTag(keys.get(position));
        holder.mName.setText(pat.name);
        holder.mAge.setText(pat.age);
        holder.mBloodgroup.setText(pat.blood_group);
        holder.mCnumber.setText(pat.cnumber);
        holder.mDiagnosis.setText(pat.diagnosis);
        holder.mLastRequest.setText(pat.LastRequest);
//        if (request.date.equals("URGENT")) {
//            holder.mWhen.setTextColor(0xFFFF0000);
//        }
        holder.mImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.girl));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return patients.size();
    }

}

