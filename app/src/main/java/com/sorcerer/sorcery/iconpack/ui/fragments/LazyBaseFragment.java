package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;

import java.lang.reflect.Field;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/10
 */
public class LazyBaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();

    protected LayoutInflater mInflater;
    private View mContentView;
    private Context mContext;
    private ViewGroup mContainer;
    protected BaseActivity mHoldingActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;
        onCreateView(savedInstanceState);
        if (mContentView == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return mContentView;
    }

    protected void onCreateView(Bundle savedInstanceState) {
        // implements by subclass
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContentView = null;
        mContainer = null;
        mInflater = null;
    }

    public Context getApplicationContext() {
        return mContext;
    }

    public void setContentView(int layoutResID) {
        setContentView(mInflater.inflate(layoutResID, mContainer, false));
    }

    public void setContentView(View view) {
        mContentView = view;
    }

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHoldingActivity = (BaseActivity) activity;
    }

    @Override
    public void onDetach() {
        Log.d("TAG", "onDetach() : ");
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        Log.d("TAG", "onDestroy() : ");
        super.onDestroy();
    }
}