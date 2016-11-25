package com.jexapps.bloodhub;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;



public class RecycleMarginDecoration extends RecyclerView.ItemDecoration {

    private int margin;

    public RecycleMarginDecoration(Context context) {
  /* Assign value from xml whatever you want to make as margin*/
        margin = context.getResources().getDimensionPixelSize(R.dimen.padding_eight);
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin / 2, margin / 2, 0);
    }
}
