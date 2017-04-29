package com.sorcerer.sorcery.iconpack.search;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.materialize.util.KeyboardUtil;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.anim.FadeInTransition;
import com.sorcerer.sorcery.iconpack.ui.anim.FadeOutTransition;
import com.sorcerer.sorcery.iconpack.ui.anim.SimpleTransitionListener;
import com.sorcerer.sorcery.iconpack.ui.anim.ViewFader;
import com.sorcerer.sorcery.iconpack.ui.views.SearchBar;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.activity_search)
    ViewGroup mRootView;

    @BindView(R.id.imageView_search_graphic)
    ImageView mSearchGraphic;

    @BindView(R.id.searchBar)
    SearchBar mSearchBar;

    @BindView(R.id.relativeLayout_content_search)
    RelativeLayout mContent;

    @BindView(R.id.recyclerView_search)
    RecyclerView mRecyclerView;

    @BindView(R.id.textView_search_hint)
    TextView mHintTextView;

    private SearchAdapter mAdapter;

    private ViewFader mViewFader = new ViewFader();

    interface SearchCallback {
        void call(int code);
    }

    public static final int SEARCH_CODE_OK = 0x0;
    public static final int SEARCH_CODE_INVALID_INPUT = 0x1;
    public static final int SEARCH_CODE_EMPTY = 0x2;
    public static final int SEARCH_CODE_NOT_FOUND = 0x3;

    SearchCallback mSearchCallback = new SearchCallback() {
        @Override
        public void call(int code) {
            switch (code) {
                case SEARCH_CODE_EMPTY:
                    mRecyclerView.setVisibility(INVISIBLE);
                    mHintTextView.setVisibility(GONE);
                    mSearchGraphic.setVisibility(VISIBLE);
                    break;
                case SEARCH_CODE_INVALID_INPUT:
                    mRecyclerView.setVisibility(INVISIBLE);
                    mHintTextView.setText(R.string.search_hint_only_letter);
                    mHintTextView.setVisibility(View.VISIBLE);
                    mSearchGraphic.setVisibility(GONE);
                    break;
                case SEARCH_CODE_NOT_FOUND:
                    mRecyclerView.setVisibility(INVISIBLE);
                    mHintTextView.setText(ResourceUtil.getString(SearchActivity.this,
                            R.string.search_hint_not_found)
                            + new String(Character.toChars(0x1F614)));
                    mHintTextView.setVisibility(View.VISIBLE);
                    mSearchGraphic.setVisibility(GONE);
                    break;
                default:
                    mRecyclerView.setVisibility(VISIBLE);
                    mHintTextView.setVisibility(GONE);
                    mSearchGraphic.setVisibility(GONE);
                    mAdapter.notifyDataSetChanged();
            }
            mSearchBar.setSearching(false);
        }
    };

    @Override
    protected ViewGroup rootView() {
        return mRootView;
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSupportActionBar(mSearchBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (savedInstanceState == null) {
            mViewFader.hideContentOf(mSearchBar);
            ViewTreeObserver viewTreeObserver = mSearchBar.getViewTreeObserver();
            viewTreeObserver
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mSearchBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            animateShowSearch();
                        }

                        private void animateShowSearch() {
                            TransitionManager.beginDelayedTransition(mSearchBar,
                                    FadeInTransition.createTransition());
                            mContent.animate().alpha(1).setDuration(250).start();

                            mViewFader.showContent(mSearchBar);
                        }
                    });
        } else {
            mContent.animate().alpha(1).setDuration(250).start();
        }

        mSearchBar.setSearchListener(text -> {
            if (text != null && text.length() > 0) {
                mSearchGraphic.setVisibility(GONE);
            } else {
                mSearchGraphic.setVisibility(VISIBLE);
            }

            mSearchBar.setSearching(true);
            if (text == null) {
                mAdapter.search(null);
            } else {
                mAdapter.search(text.toLowerCase());
            }
        });

        mAdapter = new SearchAdapter(this);
        mAdapter.setSearchCallback(mSearchCallback);
        mAdapter.setCustomPicker(getIntent().getBooleanExtra("custom picker", false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        int spanCount = calSpanCount(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount, VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setOnTouchListener((view, motionEvent) -> {
            KeyboardUtil.hideKeyboard(SearchActivity.this);
            return false;
        });

        mSearchBar.setHint(ResourceUtil.getString(this, R.string.search_edit_hint));
        mSearchBar.postDelayed(() -> mSearchBar.requestEditTextFocus(), 250);

        initAdapterData();
    }

    @Override
    public void finish() {
        exitTransitionWithAction(() -> {
            SearchActivity.super.finish();
            overridePendingTransition(0, 0);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler mFinishHandler = new Handler();

    private Runnable mFinishRunnable = this::finish;

    @Override
    protected void onStart() {
        super.onStart();
        mFinishHandler.removeCallbacks(mFinishRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFinishHandler.postDelayed(mFinishRunnable, 10 * 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private static int calSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float iconMargin = activity.getResources().getDimension(R.dimen.icon_grid_item_margin);
        float cardMargin = activity.getResources().getDimension(R.dimen.icon_grid_card_margin);
        float iconSize =
                activity.getResources().getDimension(R.dimen.icon_grid_item_size) + 2 * iconMargin;
        return (int) ((size.x - 2 * cardMargin - 2 * iconMargin) / iconSize);
    }

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
        mContent.animate().alpha(0).setDuration(250).start();
    }

    private void initAdapterData() {
        Observable.create((ObservableOnSubscribe<Map<String, List<String>>>)
                e -> e.onNext(PackageUtil.getPackageWithDrawableList(SearchActivity.this))
        )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringStringMap -> {
                    Timber.d(Arrays.toString(stringStringMap.entrySet().toArray())
                            .replace(",", "\n"));
                    mAdapter.setPackageDrawableMap(stringStringMap);
                    mAdapter.search(mSearchBar.getText());
                }, Timber::d);
    }
}
