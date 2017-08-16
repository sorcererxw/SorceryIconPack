package com.sorcerer.sorcery.iconpack.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/29 0029
 */
public abstract class BaseFastRecyclerAdapter<VH extends BaseFastRecyclerAdapter.BaseViewHolder, M>
        extends RecyclerView.Adapter<VH> {

    protected final String TAG = getClass().getSimpleName();
    protected Context mContext;
    protected RecyclerView mHoldingRecyclerView;
    protected List<M> mDataList;

    public BaseFastRecyclerAdapter(Context context, List<M> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mHoldingRecyclerView = recyclerView;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
