package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.wang.avi.AVLoadingIndicatorView;

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

                AVLoadingIndicatorView indicator =
                        (AVLoadingIndicatorView) view.findViewById(R.id.indicator);
                indicator.setIndicatorColor(
                        ResourceUtil.getAttrColor(getContext(), android.R.attr.colorAccent));

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
    public void setContentView(final View view) {
        // if isLazyLoad==true, remove lazy view to load real view
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                Animation animation = new AlphaAnimation(1, 0);
                animation.setDuration(200);
                if (i == layout.getChildCount() - 1) {
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout.removeAllViews();
                            layout.addView(view);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                layout.getChildAt(i).startAnimation(animation);
            }
        }
        // else load directly
        else {
            super.setContentView(view);
        }
    }

    @Deprecated
    @Override
    public final void onStart() {
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
        if (isInit && isStart && getUserVisibleHint()) {
            isStart = false;
            onFragmentStopLazy();
        }
    }

    // on fragment show
    protected void onFragmentStartLazy() {
    }

    // on fragment dismiss
    protected void onFragmentStopLazy() {

    }

    protected void onCreateViewLazy(Bundle savedInstanceState) {

    }

    protected void onResumeLazy() {
    }

    protected void onPauseLazy() {

    }

    protected void onDestroyViewLazy() {

    }

    @Override
    @Deprecated
    public final void onResume() {

        super.onResume();
        if (isInit) {
            onResumeLazy();
        }
    }

    @Override
    @Deprecated
    public final void onPause() {
        super.onPause();
        if (isInit) {
            onPauseLazy();
        }
    }

    @Override
    @Deprecated
    public final void onDestroyView() {
        super.onDestroyView();
        if (isInit) {
            onDestroyViewLazy();
        }
        isInit = false;
    }
}