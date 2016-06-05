package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/5/29 0029.
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;

    protected BaseActivity mHoldingActivity;

    protected abstract int provideLayoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(provideLayoutId(), container, false);
        mContext = getContext();
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    protected abstract void init();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHoldingActivity = (BaseActivity) activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
