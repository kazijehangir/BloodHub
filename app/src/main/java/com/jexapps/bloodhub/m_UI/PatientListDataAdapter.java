package com.jexapps.bloodhub.m_UI;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jexapps.bloodhub.AddRequestOrgActivity;
import com.jexapps.bloodhub.R;
import com.jexapps.bloodhub.m_Model.Patient;

import java.util.ArrayList;

//import com.jexapps.bloodhub.PatientDetail;

/**
 * Created by aimengaba on 23/07/2017.
 */

public class PatientListDataAdapter extends RecyclerView.Adapter<PatientListDataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Patient> patients;
    private ArrayList<String> keys;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AddRequestOrgActivity.class);
            intent.putExtra("patient", (String) v.getTag());
            v.getContext().startActivity(intent);

//            TextView mName = (TextView) itemView.findViewById(R.id.name_text);
//            intent.putExtra("name", mName.getText());
//
//            TextView mAge = (TextView) itemView.findViewById(R.id.age_text);
//            intent.putExtra("age", mAge.getText());
//
//            TextView mBloodgroup = (TextView) itemView.findViewById(R.id.bgroup_text);
//            intent.putExtra("blood_group", mBloodgroup.getText());
//
//            TextView mCnumber = (TextView) itemView.findViewById(R.id.number_text);
//            intent.putExtra("con_number", mCnumber.getText());
//
//            TextView mDiagnosis = (TextView) itemView.findViewById(R.id.diagnosis_text);
//            intent.putExtra("diagnosis", mDiagnosis.getText());

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PatientListDataAdapter(ArrayList<Patient> pat, ArrayList<String> list ,Context context) {
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

