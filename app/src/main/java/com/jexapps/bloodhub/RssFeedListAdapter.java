package com.jexapps.bloodhub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kazi on 16/11/17.
 */
public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RssFeedModel> mRssFeedModels;
    private Context context;


    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public RssFeedListAdapter(FragmentActivity context, List<RssFeedModel> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
        this.context = context;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_rss_feed, null);

//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_rss_feed, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);
        ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText))
                .setText(rssFeedModel.description);
//        ((TextView)holder.rssFeedView.findViewById(R.id.linkText)).setText(rssFeedModel.link);
        (holder.rssFeedView.findViewById(R.id.rss_card_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssFeedModel.link));
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }
}
