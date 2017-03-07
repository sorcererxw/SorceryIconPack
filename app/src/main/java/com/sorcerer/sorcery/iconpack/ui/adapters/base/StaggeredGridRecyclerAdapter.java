package com.sorcerer.sorcery.iconpack.ui.adapters.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.BOTTOM_LEFT;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.BOTTOM_RIGHT;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.CENTER;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.EDGE_BOTTOM;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.EDGE_LEFT;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.EDGE_RIGHT;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.EDGE_TOP;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.ONE_COL_BOTTOM;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.ONE_COL_MID;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.ONE_COL_TOP;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.ONE_ROW_LEFT;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.ONE_ROW_MID;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.ONE_ROW_RIGHT;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.TOP_LEFT;
import static com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter.ItemPosType.TOP_RIGHT;

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
                return ONE_COL_TOP;
            }
            if (pos == itemNum - 1) {
                return ONE_COL_BOTTOM;
            }
            return ONE_COL_MID;
        }

        if (itemNum <= mSpan) {
            if (pos == 0) {
                return ONE_ROW_LEFT;
            }
            if (pos == itemNum - 1) {
                return ONE_ROW_RIGHT;
            }
            return ONE_ROW_MID;
        }

        if (pos < span) {
            if (index == 0) {
                return TOP_LEFT;
            }
            if (index == span - 1) {
                return TOP_RIGHT;
            }
            return EDGE_TOP;
        }
        if (pos == itemNum - 1) {
            if (index == 0) {
                return BOTTOM_LEFT;
            }
            if (index == span - 1) {
                return BOTTOM_RIGHT;
            }
            return EDGE_BOTTOM;
        }
        if (index == 0) {
            return EDGE_LEFT;
        }
        if (index == mSpan - 1) {
            return EDGE_RIGHT;
        }
        return CENTER;
    }
}
