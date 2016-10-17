package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/6/3 0003
 */
public class LazyFragment extends LazyBaseFragment {
    private boolean isInit = false; // is real view loaded
    Bundle savedInstanceState;
    public static final String INTENT_BOOLEAN_LAZYLOAD = "intent_boolean_lazyLoad";
    private boolean isLazyLoad = true;
    private FrameLayout layout;
    private boolean isStart = false; // is fragment visible

    @Override
    protected final void onCreateView(Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onCreateView(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isLazyLoad = bundle.getBoolean(INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        }
        if (isLazyLoad) {
            if (getUserVisibleHint() && !isInit) {
                // visible and not init
                this.savedInstanceState = savedInstanceState;
                onCreateViewLazy(savedInstanceState);
                isInit = true;
            } else {
                // lazy load
                layout = new FrameLayout(getApplicationContext());
                layout.setLayoutParams(
                        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

                View view = View.inflate(getApplicationContext(),
                        R.layout.fragment_lazy_loading, null);

                layout.addView(view);
                super.setContentView(layout);
            }
        } else {
            onCreateViewLazy(savedInstanceState);
            isInit = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TAG", "setUserVisibleHint() called with: " + "isVisibleToUser = [" + isVisibleToUser
                + "]");

        if (isVisibleToUser && !isInit && getContentView() != null) {
            // visible but not init
            onCreateViewLazy(savedInstanceState);
            isInit = true;
            onResumeLazy();
        }
        if (isInit && getContentView() != null) {
            // has init
            if (isVisibleToUser) {
                isStart = true;
                onFragmentStartLazy();
            } else {
                isStart = false;
                onFragmentStopLazy();
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = mInflater.inflate(layoutResID, layout, false);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        // if isLazyLoad==true, remove lazy view to load real view
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            layout.addView(view);
        }
        // else load directly
        else {
            super.setContentView(view);
        }
    }

    @Deprecated
    @Override
    public final void onStart() {
        Log.d("TAG", "onStart() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onStart();
        if (isInit && !isStart && getUserVisibleHint()) {
            isStart = true;
            onFragmentStartLazy();
        }
    }

    @Deprecated
    @Override
    public final void onStop() {
        super.onStop();
        Log.d("TAG", "onStop() called: " + "getUserVisibleHint():" + getUserVisibleHint());
        if (isInit && isStart && getUserVisibleHint()) {
            isStart = false;
            onFragmentStopLazy();
        }
    }

    // on fragment show
    protected void onFragmentStartLazy() {
        Log.d("TAG", "onFragmentStartLazy() called with: " + "");
    }

    // on fragment dismiss
    protected void onFragmentStopLazy() {
        Log.d("TAG", "onFragmentStopLazy() called with: " + "");
    }

    protected void onCreateViewLazy(Bundle savedInstanceState) {
        Log.d("TAG", "onCreateViewLazy() called with: "
                + "savedInstanceState = [" + savedInstanceState + "]");
    }

    protected void onResumeLazy() {
        Log.d("TAG", "onResumeLazy() called with: " + "");
    }

    protected void onPauseLazy() {
        Log.d("TAG", "onPauseLazy() called with: " + "");
    }

    protected void onDestroyViewLazy() {

    }

    @Override
    @Deprecated
    public final void onResume() {
        Log.d("TAG", "onResume() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onResume();
        if (isInit) {
            onResumeLazy();
        }
    }

    @Override
    @Deprecated
    public final void onPause() {
        Log.d("TAG", "onPause() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onPause();
        if (isInit) {
            onPauseLazy();
        }
    }

    @Override
    @Deprecated
    public final void onDestroyView() {
        Log.d("TAG", "onDestroyView() : " + "getUserVisibleHint():" + getUserVisibleHint());
        super.onDestroyView();
        if (isInit) {
            onDestroyViewLazy();
        }
        isInit = false;
    }
}