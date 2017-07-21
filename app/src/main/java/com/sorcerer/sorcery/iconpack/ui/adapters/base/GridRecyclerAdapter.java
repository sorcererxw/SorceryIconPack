package com.sorcerer.sorcery.iconpack.ui.adapters.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/6/4 0004
 */
public abstract class GridRecyclerAdapter<VH extends BaseFastRecyclerAdapter.BaseViewHolder, M>
        extends BaseFastRecyclerAdapter<VH, M> {

    protected int mSpan;

    public GridRecyclerAdapter(Context context, List<M> dataList, int defaultSpan) {
        super(context, dataList);
        mSpan = defaultSpan;
    }

    /**
     * @param target
     * @param pos
     * @param times  one margin is 4dp * times
     */
    protected void setViewMargin(View target, int pos,
                                 int times, RecyclerView.ViewHolder viewHolder) {
        ItemPosType type = calculateItemPosType(pos, mSpan, getItemCount(), viewHolder);
        ItemMargin itemMargin = getItemMargin(type);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(
                times * dp2px((int) itemMargin.left),
                times * dp2px((int) itemMargin.top),
                times * dp2px((int) itemMargin.right),
                times * dp2px((int) itemMargin.bottom)
        );
        target.setLayoutParams(params);
    }

    private ItemMargin getItemMargin(ItemPosType type) {
        switch (type) {
            case ONE_COL_TOP:
                return new ItemMargin(-0.5, 0, -0.5, 1);
            case ONE_COL_MID:
                return new ItemMargin(-0.5, 1, -0.5, 1);
            case ONE_COL_BOTTOM:
                return new ItemMargin(-0.5, 1, -0.5, 2);
            case ONE_ROW_LEFT:
                return new ItemMargin(2, 2, 1, 2);
            case ONE_ROW_MID:
                return new ItemMargin(1, 2, 1, 2);
            case ONE_ROW_RIGHT:
                return new ItemMargin(1, 2, 2, 2);
            case TOP_LEFT:
                return new ItemMargin(2, 2, 1, 1);
            case TOP_RIGHT:
                return new ItemMargin(1, 2, 2, 1);
            case BOTTOM_LEFT:
                return new ItemMargin(2, 1, 1, 2);
            case BOTTOM_RIGHT:
                return new ItemMargin(1, 1, 2, 2);
            case EDGE_TOP:
                return new ItemMargin(1, 2, 1, 1);
            case EDGE_BOTTOM:
                return new ItemMargin(1, 1, 1, 2);
            case EDGE_LEFT:
                return new ItemMargin(2, 1, 1, 1);
            case EDGE_RIGHT:
                return new ItemMargin(1, 1, 2, 1);
            case CENTER:
                return new ItemMargin(1, 1, 1, 1);
            default:
                return new ItemMargin(1, 1, 1, 1);
        }
    }

    public void changeSpan(int span) {
        mSpan = span;
        notifyDataSetChanged();
    }

    protected ItemPosType calculateItemPosType(int pos, int span, int itemNum,
                                               RecyclerView.ViewHolder viewHolder) {
        if (span == 1) {
            if (pos == 0) {
                return ItemPosType.ONE_COL_TOP;
            } else if (pos == itemNum - 1) {
                return ItemPosType.ONE_COL_BOTTOM;
            } else {
                return ItemPosType.ONE_COL_MID;
            }
        } else if (span == itemNum) {
            if (pos == 0) {
                return ItemPosType.ONE_ROW_LEFT;
            } else if (pos == itemNum - 1) {
                return ItemPosType.ONE_ROW_RIGHT;
            } else {
                return ItemPosType.ONE_ROW_MID;
            }
        } else {
            if (pos < span) {
                if (pos == 0) {
                    return ItemPosType.TOP_LEFT;
                } else if (pos == span - 1) {
                    return ItemPosType.TOP_RIGHT;
                } else {
                    return ItemPosType.EDGE_TOP;
                }
            } else if (pos / span * span + span >= itemNum) {
                int linePos = pos % span;
                if (linePos == 0) {
                    return ItemPosType.BOTTOM_LEFT;
                } else if (linePos == span - 1) {
                    return ItemPosType.BOTTOM_RIGHT;
                } else {
                    return ItemPosType.EDGE_BOTTOM;
                }
            } else {
                int linePos = pos % span;
                if (linePos == 0) {
                    return ItemPosType.EDGE_LEFT;
                } else if (linePos == span - 1) {
                    return ItemPosType.EDGE_RIGHT;
                } else {
                    return ItemPosType.CENTER;
                }
            }
        }
    }

    private int dp2px(float dp) {
        if (dp == 1) {
            return 1;
        }
        return (int) UIUtils.convertDpToPixel(dp, mContext);
    }

    enum ItemPosType {
        ONE_COL_TOP,
        ONE_COL_MID,
        ONE_COL_BOTTOM,

        ONE_ROW_LEFT,
        ONE_ROW_MID,
        ONE_ROW_RIGHT,

        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        EDGE_TOP,
        EDGE_BOTTOM,
        EDGE_LEFT,
        EDGE_RIGHT,
        CENTER
    }

    private class ItemMargin {
        double left;
        double right;
        double top;
        double bottom;

        ItemMargin(double left, double top, double right, double bottom) {
            this.left = 2 * left;
            this.top = 2 * top;
            this.right = 2 * right;
            this.bottom = 2 * bottom;
        }
    }

}
