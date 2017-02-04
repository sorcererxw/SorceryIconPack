package com.sorcerer.sorcery.iconpack.ui.anim;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.Navigator;
import com.sorcerer.sorcery.iconpack.ui.views.ExposedSearchToolbar;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

public class SearchTransitioner {
    private Activity mActivity;
    private ExposedSearchToolbar mSearchToolbar;
    private Navigator mNavigator;
    private TabLayout mTabLayout;
    private ViewGroup mActivityContent;
    private ViewFader mViewFader;

    private int mToolbarMargin;
    private boolean mTransitioning;

    public SearchTransitioner(Activity activity,
                              Navigator navigator,
                              TabLayout tabLayout,
                              ViewGroup activityContent,
                              ExposedSearchToolbar searchToolbar,
                              ViewFader viewFader) {
        mActivity = activity;
        mNavigator = navigator;
        mTabLayout = tabLayout;
        mActivityContent = activityContent;
        mSearchToolbar = searchToolbar;
        mViewFader = viewFader;
        mToolbarMargin = activity.getResources().getDimensionPixelSize(R.dimen.padding_tight);
    }

    public void transitionToSearch() {
        if (mTransitioning) {
            return;
        }
        if (supportTransitions()) {
            Transition transition = FadeOutTransition.withAction(navigateToSearchWhenDone());
            TransitionManager.beginDelayedTransition(mSearchToolbar, transition);
            expandToolbar();
            mViewFader.hideContentOf(mSearchToolbar);
            mTabLayout.animate().translationYBy(-mTabLayout.getHeight()).setDuration(250).start();
            mActivityContent.animate().alpha(0).setDuration(250).start();
        } else {
            mNavigator.toSearch();
        }
    }

    private void expandToolbar() {
        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams) mSearchToolbar.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        mSearchToolbar.setLayoutParams(layoutParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Transition.TransitionListener navigateToSearchWhenDone() {
        return new SimpleTransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mTransitioning = true;
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                mTransitioning = false;
                mNavigator.toSearch();
                mActivity.overridePendingTransition(0, 0);
            }
        };
    }

    public static boolean supportTransitions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public void onActivityResumed() {
        if (supportTransitions()) {
            TransitionManager
                    .beginDelayedTransition(mSearchToolbar, FadeInTransition.createTransition());
            FrameLayout.LayoutParams layoutParams =
                    (FrameLayout.LayoutParams) mSearchToolbar.getLayoutParams();
            layoutParams.setMargins(mToolbarMargin, mToolbarMargin, mToolbarMargin, mToolbarMargin);
            mViewFader.showContent(mSearchToolbar);
            mSearchToolbar.setLayoutParams(layoutParams);
            mTabLayout.animate().translationY(0).setDuration(250).start();
            mActivityContent.animate().alpha(1).setDuration(250).start();
        }
    }

}
