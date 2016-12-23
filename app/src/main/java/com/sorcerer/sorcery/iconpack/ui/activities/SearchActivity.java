package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.SearchAdapter;
import com.sorcerer.sorcery.iconpack.ui.others.FadeInTransition;
import com.sorcerer.sorcery.iconpack.ui.others.FadeOutTransition;
import com.sorcerer.sorcery.iconpack.ui.others.SearchTransitioner;
import com.sorcerer.sorcery.iconpack.ui.others.SimpleTransitionListener;
import com.sorcerer.sorcery.iconpack.ui.others.ViewFader;
import com.sorcerer.sorcery.iconpack.ui.views.SearchBar;
import com.sorcerer.sorcery.iconpack.utils.KeyboardUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.SimpleTextWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SearchActivity extends AppCompatActivity {

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
    private GridLayoutManager mLayoutManager;

    private ViewFader mViewFader = new ViewFader();

    SearchAdapter.SearchCallback mSearchCallback = new SearchAdapter.SearchCallback() {
        @Override
        public void call(int code) {
            switch (code) {
                case SearchAdapter.SEARCH_CODE_EMPTY:
                    mRecyclerView.setVisibility(INVISIBLE);
                    mHintTextView.setVisibility(GONE);
                    mSearchGraphic.setVisibility(VISIBLE);
                    break;
                case SearchAdapter.SEARCH_CODE_INVALID_INPUT:
                    mRecyclerView.setVisibility(INVISIBLE);
                    mHintTextView.setText(R.string.search_hint_only_letter);
                    mHintTextView.setVisibility(View.VISIBLE);
                    mSearchGraphic.setVisibility(GONE);
                    break;
                case SearchAdapter.SEARCH_CODE_NOT_FOUND:
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(mSearchBar);

        if (savedInstanceState == null && SearchTransitioner.supportTransitions()) {
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
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                TransitionManager.beginDelayedTransition(mSearchBar,
                                        FadeInTransition.createTransition());
                                mContent.animate().alpha(1).setDuration(250).start();
                            }
                            mViewFader.showContent(mSearchBar);
                        }
                    });
        } else {
            mContent.animate().alpha(1).setDuration(250).start();
        }

        mSearchBar.addTextWatcher(new SimpleTextWatcher() {

            @Override
            public void afterTextChanged(final Editable editable) {
                super.afterTextChanged(editable);
                if (editable != null && editable.length() > 0) {
                    mSearchGraphic.setVisibility(GONE);
                } else {
                    mSearchGraphic.setVisibility(VISIBLE);
                }

                if (editable == null) {
                    mAdapter.search(null, mSearchCallback);
                } else {
                    mAdapter.search(editable.toString().toLowerCase(), mSearchCallback);
                }
            }
        });


        mAdapter = new SearchAdapter(this);
        mAdapter.setCustomPicker(getIntent().getBooleanExtra("custom picker", false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        int spanCount = calSpanCount(this);
        mLayoutManager = new GridLayoutManager(this, spanCount, VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                KeyboardUtil.closeKeyboard(SearchActivity.this);
                return false;
            }
        });

        mSearchBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchBar.requestEditTextFocus();
            }
        }, 250);

        getIconBeanList();
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
        }
        return super.onOptionsItemSelected(item);
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
        mContent.animate().alpha(0).setDuration(250).start();
    }

    private void getIconBeanList() {
        Observable.create(new ObservableOnSubscribe<List<IconBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<IconBean>> emitter) throws Exception {
                List<IconBean> list = new ArrayList<>();
                for (String name : ResourceUtil.getStringArray(SearchActivity.this, "icon_pack")) {
                    if (name.startsWith("**")) {
                        continue;
                    }
                    IconBean iconBean = new IconBean(name);
                    int res = getResources().getIdentifier(name, "drawable", getPackageName());
                    if (res != 0) {
                        final int thumbRes =
                                getResources().getIdentifier(name, "drawable", getPackageName());
                        if (thumbRes != 0) {
                            iconBean.setRes(thumbRes);
                        }
                    }
                    list.add(iconBean);
                }
                emitter.onNext(list);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<IconBean>>() {

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<IconBean> iconBeen) {
                        mAdapter.setData(iconBeen);
                        mAdapter.search(mSearchBar.getText(), mSearchCallback);
                    }
                });
    }
}
