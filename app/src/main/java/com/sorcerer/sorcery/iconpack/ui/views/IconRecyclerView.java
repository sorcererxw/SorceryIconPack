package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/5 0005
 */
public class IconRecyclerView extends RecyclerView {

    private View mEmptyView;

    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            Adapter adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    setVisibility(INVISIBLE);
                    mEmptyView.setVisibility(VISIBLE);
                } else {
                    setVisibility(VISIBLE);
                    mEmptyView.setVisibility(INVISIBLE);
                }
            }
        }
    };

    public IconRecyclerView(Context context) {
        super(context);
    }

    public IconRecyclerView(Context context,
                            @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IconRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
    }

    public void removeEmptyView() {
        mEmptyView = null;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
        }

        mAdapterDataObserver.onChanged();
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

//        if (mLastState != state) {
//            Toast.makeText(getContext(), state + "", Toast.LENGTH_SHORT).show();
//            mLastState = state;
//        }
    }

}
