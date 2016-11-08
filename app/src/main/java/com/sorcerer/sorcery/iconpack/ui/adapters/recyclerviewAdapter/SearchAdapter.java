package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.activities.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.mthli.slice.Slice;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/7
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<IconBean> mDataList;
    private List<IconBean> mShowList;

    private int mSpan;
    private Activity mActivity;

    public SearchAdapter(Activity activity, int span) {
        mActivity = activity;
        mSpan = span;
        mDataList = new ArrayList<>();
        mShowList = new ArrayList<>();
    }

    public void setSpan(int span) {
        if (span == mSpan) {
            return;
        }
        mSpan = span;
        notifyDataSetChanged();
    }

    public void setData(List<IconBean> dataList) {
        mDataList = dataList;
    }

    private static final int TYPE_CENTER = 0x1;
    private static final int TYPE_TOP = 0x2;
    private static final int TYPE_TOP_LEFT = 0x3;
    private static final int TYPE_TOP_RIGHT = 0x4;
    private static final int TYPE_BOTTOM = 0x5;
    private static final int TYPE_BOTTOM_LEFT = 0x6;
    private static final int TYPE_BOTTOM_RIGHT = 0x7;
    private static final int TYPE_LEFT = 0x8;
    private static final int TYPE_RIGHT = 0x9;
    private static final int TYPE_SINGLE_LINE_LEFT = 0x10;
    private static final int TYPE_SINGLE_LINE_CENTER = 0x11;
    private static final int TYPE_SINGLE_LINE_RIGHT = 0x12;

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() <= mSpan) {
            if (position == 0) {
                return TYPE_SINGLE_LINE_LEFT;
            }
            if (position == getItemCount() - 1) {
                return TYPE_SINGLE_LINE_RIGHT;
            }
            return TYPE_SINGLE_LINE_CENTER;
        }
        if (position == 0) {
            return TYPE_TOP_LEFT;
        }
        if (position == mSpan - 1) {
            return TYPE_TOP_RIGHT;
        }
        if (position == getItemCount() - mSpan) {
            return TYPE_BOTTOM_LEFT;
        }
        if (position == getItemCount() - 1) {
            return TYPE_BOTTOM_RIGHT;
        }
        if (position < mSpan) {
            return TYPE_TOP;
        }
        if (position + mSpan >= getItemCount()) {
            return TYPE_BOTTOM;
        }
        if ((position + 1) % mSpan == 0) {
            return TYPE_RIGHT;
        }
        if (position % mSpan == 0) {
            return TYPE_LEFT;
        }
        return TYPE_CENTER;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case TYPE_SINGLE_LINE_CENTER:
                layout = R.layout.item_icon_single_line_center;
                break;
            case TYPE_SINGLE_LINE_LEFT:
                layout = R.layout.item_icon_single_line_left;
                break;
            case TYPE_SINGLE_LINE_RIGHT:
                layout = R.layout.item_icon_single_line_right;
                break;
            case TYPE_CENTER:
                layout = R.layout.item_icon_center;
                break;
            case TYPE_TOP:
                layout = R.layout.item_icon_top;
                break;
            case TYPE_TOP_LEFT:
                layout = R.layout.item_icon_top_left;
                break;
            case TYPE_TOP_RIGHT:
                layout = R.layout.item_icon_top_right;
                break;
            case TYPE_BOTTOM:
                layout = R.layout.item_icon_bottom;
                break;
            case TYPE_BOTTOM_LEFT:
                layout = R.layout.item_icon_bottom_left;
                break;
            case TYPE_BOTTOM_RIGHT:
                layout = R.layout.item_icon_bottom_right;
                break;
            case TYPE_LEFT:
                layout = R.layout.item_icon_left;
                break;
            case TYPE_RIGHT:
                layout = R.layout.item_icon_right;
                break;
            default:
                layout = R.layout.item_icon_center;
        }
        return new SearchViewHolder(
                LayoutInflater.from(mActivity).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        if (position < mShowList.size()) {
            Glide.with(mActivity)
                    .load(mShowList.get(position).getRes())
                    .into(holder.icon);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showIconDialog(holder.icon, mShowList.get(holder.getAdapterPosition()));
                }
            });
        }

        boolean leftBottomRect = false,
                rightBottomRect = false,
                leftTopRect = false,
                rightTopRect = false,
                leftEdgeShadow = false,
                rightEdgeShadow = false,
                bottomEdgeShadow = false,
                topEdgeShadow = false;
        if (position == 0) {
            leftTopRect = true;
        }
        if (position == mSpan - 1) {
            rightTopRect = true;
        }
        if (position == getItemCount() - mSpan) {
            leftBottomRect = true;
        }
        if (position == getItemCount() - 1) {
            rightBottomRect = true;
        }

        if (position < mSpan) {
            topEdgeShadow = true;
        }
        if (position + mSpan >= getItemCount()) {
            bottomEdgeShadow = true;
        }
        if ((position + 1) % mSpan == 0) {
            rightEdgeShadow = true;
        }
        if (position % mSpan == 0) {
            leftEdgeShadow = true;
        }

        holder.slice.showLeftBottomRect(!leftBottomRect);
        holder.slice.showRightBottomRect(!rightBottomRect);
        holder.slice.showLeftTopRect(!leftTopRect);
        holder.slice.showRightTopRect(!rightTopRect);
        holder.slice.showLeftEdgeShadow(leftEdgeShadow);
        holder.slice.showRightEdgeShadow(rightEdgeShadow);
        holder.slice.showBottomEdgeShadow(bottomEdgeShadow);
        holder.slice.showTopEdgeShadow(topEdgeShadow);
    }

    public void search(String s) {
        mShowList.clear();
        if (s == null || s.length() == 0) {
            mShowList.clear();
        } else {
            for (IconBean iconBean : mDataList) {
                if (iconBean.getLabel().contains(s)) {
                    mShowList.add(iconBean);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mShowList.size() % mSpan == 0) {
            return mShowList.size();
        } else {
            return (mShowList.size() + mSpan) / mSpan * mSpan;
        }
    }

    private void showIconDialog(ImageView icon, IconBean iconBean) {
        Intent intent = new Intent(mActivity, IconDialogActivity.class);
        intent.putExtra(IconDialogActivity.EXTRA_RES, iconBean.getRes());
        intent.putExtra(IconDialogActivity.EXTRA_NAME, iconBean.getName());
        intent.putExtra(IconDialogActivity.EXTRA_LABEL, iconBean.getLabel());
        if (Build.VERSION.SDK_INT >= 21) {
            mActivity.startActivityForResult(
                    intent,
                    MainActivity.REQUEST_ICON_DIALOG,
                    ActivityOptions.makeSceneTransitionAnimation(
                            mActivity,
                            icon,
                            "icon"
                    ).toBundle()
            );
        } else {
            mActivity.startActivityForResult(intent, MainActivity.REQUEST_ICON_DIALOG);
            mActivity.overridePendingTransition(R.anim.fast_fade_in, 0);
        }
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.frameLayout_item_icon_container)
        FrameLayout container;

        @BindView(R.id.imageView_item_icon)
        ImageView icon;

        Slice slice;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            slice = new Slice(container);
            slice.setRadius(2);
            slice.setRipple(0);
        }

    }
}
