package com.sorcerer.sorcery.iconpack.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.exposedSearch.FadeInTransition;
import com.sorcerer.sorcery.iconpack.ui.exposedSearch.FadeOutTransition;
import com.sorcerer.sorcery.iconpack.ui.exposedSearch.OnBackKeyPressListener;
import com.sorcerer.sorcery.iconpack.ui.exposedSearch.SearchBar;
import com.sorcerer.sorcery.iconpack.ui.exposedSearch.SearchTransitioner;
import com.sorcerer.sorcery.iconpack.ui.exposedSearch.SimpleTransitionListener;
import com.sorcerer.sorcery.iconpack.ui.exposedSearch.ViewFader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.searchBar)
    SearchBar mSearchBar;

    @BindView(R.id.linearLayout_content_search)
    LinearLayout mContent;

    private ViewFader mViewFader = new ViewFader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(mSearchBar);
        mSearchBar.setOnBackKeyPressedListener(new OnBackKeyPressListener() {
            @Override
            public boolean onBackKeyPressed() {
                SearchActivity.this.finish();
                return true;
            }
        });

        if (savedInstanceState == null && SearchTransitioner.supportTransitions()) {
            mViewFader.hideContentOf(mSearchBar);
            ViewTreeObserver viewTreeObserver = mSearchBar.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mSearchBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    animateShowSearch();
                }

                private void animateShowSearch() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(mSearchBar, FadeInTransition.createTransition());
                    }
                    mViewFader.showContent(mSearchBar);
                }
            });
        }
    }

    @Override
    public void finish() {
        if (SearchTransitioner.supportTransitions()) {
            exitTransitionWithAction(new Runnable() {
                @Override
                public void run() {
                    SearchActivity.super.finish();
                    overridePendingTransition(0, 0);
                }
            });
        } else {
            super.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
//            case R.id.action_clear:
//                onClearPressed();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void exitTransitionWithAction(final Runnable endingAction) {
        Transition transition = FadeOutTransition.withAction(new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                endingAction.run();
            }
        });
        TransitionManager.beginDelayedTransition(mSearchBar, transition);
        mViewFader.hideContentOf(mSearchBar);
        TransitionManager.beginDelayedTransition(mContent, new Fade(Fade.OUT));
    }
}
