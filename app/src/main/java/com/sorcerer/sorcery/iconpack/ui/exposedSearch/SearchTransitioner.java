package com.sorcerer.sorcery.iconpack.ui.exposedSearch;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

public class SearchTransitioner {
    private Activity mActivity;
    private ExposedSearchToolbar mSearchToolbar;
    private Navigator mNavigator;
    //    private ViewGroup mActivityContent;
    private ViewGroup[] mActivityContents;
    private ViewFader mViewFader;

    private int mToolbarMargin;
    private boolean mTransitioning;

    public SearchTransitioner(Activity activity,
                              Navigator navigator,
//                              ViewGroup activityContent,
                              ViewGroup[] activityContents,
                              ExposedSearchToolbar searchToolbar,
                              ViewFader viewFader) {
        mActivity = activity;
        mNavigator = navigator;
//        mActivityContent = activityContent;
        mActivityContents = activityContents;
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
            for (ViewGroup viewGroup : mActivityContents) {
                if (viewGroup instanceof TabLayout) {
                    viewGroup.animate().translationYBy(-viewGroup.getHeight()).setDuration(250)
                            .start();
                } else {
                    viewGroup.animate().alpha(0).setDuration(250).start();
                }
            }

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

            for (ViewGroup viewGroup : mActivityContents) {
                if (viewGroup instanceof TabLayout) {
                    viewGroup.animate().translationY(0).setDuration(250).start();
                } else {
                    viewGroup.animate().alpha(1).setDuration(250).start();
                }
            }

//            TransitionManager.beginDelayedTransition(mActivityContent, new Fade(Fade.IN));
//            mActivityContent.setVisibility(View.VISIBLE);
//            for (ViewGroup viewGroup : mActivityContents) {
//                TransitionManager.beginDelayedTransition(viewGroup, new Fade(Fade.IN));
//                viewGroup.setVisibility(View.VISIBLE);
//            }
        }
    }

}
