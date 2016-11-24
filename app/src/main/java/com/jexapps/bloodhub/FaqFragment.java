package com.jexapps.bloodhub;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FaqFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FaqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaqFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FaqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaqFragment newInstance(String param1, String param2) {
        FaqFragment fragment = new FaqFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ExpandableListView expandableListView;

    List<String> faq;
    Map<String, List<String>> topics;
    ExpandableListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = (View) inflater.inflate(R.layout.fragment_faq, container, false);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        fillData();


        listAdapter = new myExpListAdapter(this,faq,topics);

        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                Toast.makeText(MainActivity, this, faq.get(i) + " : " + topics.get(faq.get(i)).get(i1), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        return rootView;
    }

    public void fillData()
    {
        faq = new ArrayList<>();
        topics = new HashMap<>();

        faq.add("Eligible to donate Blood"); //faq0
        faq.add("Is donating blood safe?"); //faq1
        faq.add("How long will it take for the body to replenish the blood?"); //faq2
        faq.add("Time period after which you can donate blood again"); //faq3
        faq.add("Can a donor work after donating blood?"); //faq4

        List<String> faq0 = new ArrayList<>();
        List<String> faq1 = new ArrayList<>();
        List<String> faq2 = new ArrayList<>();
        List<String> faq3 = new ArrayList<>();
        List<String> faq4 = new ArrayList<>();

        faq0.add("Any person within the age group of 18-60 years with a body weight as minimum 45kgs, having haemoglobin content as minimum 12.5 gm%");
        faq1.add("Donating blood is a safe process. Needles and bags used to collect blood are used only once and then discarded");
        faq1.add("Its highly reccommended");
        faq2.add("The body replaces blood volume or plasma within 24 hours. Red cells need about 21 days for complete replacement.");
        faq3.add("A healthy person can donate after every 56 days.");
        faq3.add("So basically after every 3 months");
        faq4.add("Of course! Routine work is absolutely fine after the initial rest. Rigorous physical work should be avoided for a few hours.");

        topics.put(faq.get(0), faq0);
        topics.put(faq.get(1), faq1);
        topics.put(faq.get(2), faq2);
        topics.put(faq.get(3), faq3);
        topics.put(faq.get(4), faq4);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFaqFragmentInteraction(uri);
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
        void onFaqFragmentInteraction(Uri uri);
    }

}
