package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Sorcerer on 2016/6/3 0003.
 */
public abstract class LazyFragment extends BaseFragment {
    protected boolean mIsVisible;
    protected boolean mIsPrepared = false;

    @Override
    protected void init() {
        initViews();
        mIsPrepared = true;
    }

    protected abstract void initViews();

    protected abstract void lazyLoad();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint()) {
            lazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mIsPrepared) {
            lazyLoad();
        }
    }
}
