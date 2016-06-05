package com.sorcerer.sorcery.iconpack.ui.adapters.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/5/29 0029.
 */
public abstract class BaseRecyclerAdapter<VH extends BaseRecyclerAdapter.BaseViewHolder, M>
        extends RecyclerView.Adapter<VH> {

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;

    protected RecyclerView mHoldingRecyclerView;

    protected List<M> mDataList;

    public BaseRecyclerAdapter(Context context, List<M> dataList) {
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
}
