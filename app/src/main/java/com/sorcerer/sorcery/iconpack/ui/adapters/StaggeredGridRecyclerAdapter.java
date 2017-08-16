package com.sorcerer.sorcery.iconpack.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/9
 */

public abstract class StaggeredGridRecyclerAdapter<VH extends BaseFastRecyclerAdapter.BaseViewHolder, M>
        extends GridRecyclerAdapter<VH, M> {
    public StaggeredGridRecyclerAdapter(Context context, List<M> dataList, int defaultSpan) {
        super(context, dataList, defaultSpan);
    }

    @Override
    protected GridRecyclerAdapter.ItemPosType calculateItemPosType(int pos, int span, int itemNum,
                                                                   RecyclerView.ViewHolder viewHolder) {
        StaggeredGridLayoutManager.LayoutParams layoutParams =
                (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
        int index = layoutParams.getSpanIndex();
        if (span == 1) {
            if (pos == 0) {
                return ItemPosType.ONE_COL_TOP;
            }
            if (pos == itemNum - 1) {
                return ItemPosType.ONE_COL_BOTTOM;
            }
            return ItemPosType.ONE_COL_MID;
        }

        if (itemNum <= mSpan) {
            if (pos == 0) {
                return ItemPosType.ONE_ROW_LEFT;
            }
            if (pos == itemNum - 1) {
                return ItemPosType.ONE_ROW_RIGHT;
            }
            return ItemPosType.ONE_ROW_MID;
        }

        if (pos < span) {
            if (index == 0) {
                return ItemPosType.TOP_LEFT;
            }
            if (index == span - 1) {
                return ItemPosType.TOP_RIGHT;
            }
            return ItemPosType.EDGE_TOP;
        }
        if (pos == itemNum - 1) {
            if (index == 0) {
                return ItemPosType.BOTTOM_LEFT;
            }
            if (index == span - 1) {
                return ItemPosType.BOTTOM_RIGHT;
            }
            return ItemPosType.EDGE_BOTTOM;
        }
        if (index == 0) {
            return ItemPosType.EDGE_LEFT;
        }
        if (index == mSpan - 1) {
            return ItemPosType.EDGE_RIGHT;
        }
        return ItemPosType.CENTER;
    }
}
