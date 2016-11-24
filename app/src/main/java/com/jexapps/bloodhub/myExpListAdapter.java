package com.jexapps.bloodhub;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by aimengaba on 24/11/2016.
 */
public class myExpListAdapter extends BaseExpandableListAdapter
{

    Context context;
    List<String> faq;
    Map<String, List<String>> topics;

    public myExpListAdapter(FragmentActivity context, List<String> faq, Map<String, List<String>> topics) {
        this.context = context;
        this.topics = topics;
        this.faq = faq;
    }

    @Override
    public int getGroupCount() {
        return faq.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return topics.get(faq.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return faq.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return topics.get(faq.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup)
    {

        String faq = (String) getGroup(i);

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_parent, null);
        }

        TextView txtParent = (TextView) view.findViewById(R.id.txtParent);
        txtParent.setText(faq);

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup)
    {

        String topic = (String) getChild(i, i1);

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_child, null);
        }

        TextView txtChild = (TextView) view.findViewById(R.id.txtChild);
        txtChild.setText(topic);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
