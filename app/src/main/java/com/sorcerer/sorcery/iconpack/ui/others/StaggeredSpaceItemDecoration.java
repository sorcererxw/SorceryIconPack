package com.sorcerer.sorcery.iconpack.ui.others;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/9
 */

public class StaggeredSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;

    public StaggeredSpaceItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)
                view.getLayoutParams();
        int spanIndex = lp.getSpanIndex();

        if (position > 0) {
            if (spanIndex == 1) {
                outRect.left = mSpace;
            } else {
                outRect.right = mSpace;
            }

            outRect.bottom = mSpace * 2;
        }
    }
}
