package com.techcamino.telecalling.util;

import androidx.annotation.Px;
import androidx.recyclerview.widget.RecyclerView;

public class CrouselView extends RecyclerView.ItemDecoration {

    private float mInterItemsGap;
    private float mNetOneSidedGap;

    public CrouselView(@Px int totalWidth, float itemWidth, Float itemPeekingPercent) {

        float cardPeekingWidth = (itemWidth * itemPeekingPercent + .5f);
        mInterItemsGap = (totalWidth - itemWidth) / 2;
        mNetOneSidedGap = mInterItemsGap / 2 - cardPeekingWidth;

    }


    private int isFirstItem( int index){return index = 0;}
    private int isLastItem(int index, RecyclerView recyclerView) {
        return index = recyclerView.getAdapter().getItemCount() - 1;
    }

}
