package com.jexapps.bloodhub;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrgDonorListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrgDonorListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrgPatientListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

//    "NAME:BGROUP:DIAGNOSIS:LASTREQUEST:STATUS:GENDER:TRANSPORT"
    private static final String[] dummyDataset = new String[] {
        "Jamshed:O-:Surgery:3 Dec, 16:Admitted:Male",
        "Aliya:A+:Thalassemia:13 Oct, 16:Visiting:Female",
        "Hamid:AB-:Accident:5 Nov, 16:Admitted:Male",
        "Saniya:B+:Cancer:10 Sept, 16:Visiting:Female"
    };
    private String mEmail;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;

    public OrgPatientListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment OrgDonorListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrgPatientListFragment newInstance(String email) {
        OrgPatientListFragment fragment = new OrgPatientListFragment();
        Bundle args = new Bundle();
        args.putString("mEmail", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmail = getArguments().getString("mEmail");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_org_patient_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.org_patient_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleMarginDecoration(getActivity()));


        // specify an adapter (see also next example)
        mAdapter = new OrgPatientListDataAdapter(dummyDataset, getContext(), mEmail);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onOrgPatientListFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onOrgPatientListFragmentInteraction(Uri uri);
    }
}
