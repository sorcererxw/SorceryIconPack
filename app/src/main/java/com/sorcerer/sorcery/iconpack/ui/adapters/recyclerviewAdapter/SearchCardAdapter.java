package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;

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

public class SearchCardAdapter
        extends RecyclerView.Adapter<SearchCardAdapter.SearchCardViewHolder> {

    private List<IconBean> mDataList;
    private List<IconBean> mShowList;
    private Context mContext;

    public SearchCardAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
        mShowList = new ArrayList<>();
    }

    public void setData(List<IconBean> dataList) {
        mDataList = dataList;
    }

    @Override
    public SearchCardViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        return new SearchCardViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_search_card, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchCardViewHolder holder, int position) {
        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams) holder.container.getLayoutParams();

        Slice slice = new Slice(holder.container);
        slice.setRadius(2);
        slice.setElevation(2.0f);
        if (position == 0) {
            slice.setRadius(8.0f);
            slice.showLeftTopRect(false);
            slice.showRightTopRect(false);
            slice.showRightBottomRect(true);
            slice.showLeftBottomRect(true);
            slice.showTopEdgeShadow(true);
            slice.showBottomEdgeShadow(false);
//            slice.showLeftTopRect(false);
//            slice.showRightTopRect(false);
//            slice.showTopEdgeShadow(true);
            layoutParams.topMargin = DisplayUtil.dip2px(mContext, 8);
        } else if (position == getItemCount() - 1) {
            slice.setRadius(8.0f);
            slice.showLeftTopRect(true);
            slice.showRightTopRect(true);
            slice.showRightBottomRect(false);
            slice.showLeftBottomRect(false);
            slice.showTopEdgeShadow(false);
            slice.showBottomEdgeShadow(true);
//            slice.showRightBottomRect(false);
//            slice.showLeftBottomRect(false);
//            slice.showBottomEdgeShadow(true);
            layoutParams.bottomMargin = DisplayUtil.dip2px(mContext, 8);
        } else {
            slice.setRadius(0.0f);
            slice.showTopEdgeShadow(false);
            slice.showBottomEdgeShadow(false);
        }


        holder.container.setLayoutParams(layoutParams);

        holder.label.setText(mShowList.get(position).getLabel());
        Glide.with(mContext)
                .load(mShowList.get(position).getRes())
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mShowList.size();
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

    static class SearchCardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linearLayout_item_search_card_container)
        LinearLayout container;

        @BindView(R.id.imageView_item_search_card_icon)
        ImageView icon;

        @BindView(R.id.textView_item_search_card_label)
        TextView label;

        public SearchCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
