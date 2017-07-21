package com.sorcerer.sorcery.iconpack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;
import com.mikepenz.materialize.util.KeyboardUtil;
import com.sorcerer.sorcery.iconpack.data.models.IconBean;
import com.sorcerer.sorcery.iconpack.iconShowCase.detail.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.network.spiders.AppSearchResultGetter;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.BaseRecyclerViewAdapter;
import com.sorcerer.sorcery.iconpack.ui.anim.FadeInTransition;
import com.sorcerer.sorcery.iconpack.ui.anim.FadeOutTransition;
import com.sorcerer.sorcery.iconpack.ui.anim.SimpleTransitionListener;
import com.sorcerer.sorcery.iconpack.ui.anim.ViewFader;
import com.sorcerer.sorcery.iconpack.ui.views.SearchBar;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SearchActivity extends BaseActivity {

    public static final int SEARCH_CODE_OK = 0x0;
    public static final int SEARCH_CODE_INVALID_INPUT = 0x1;
    public static final int SEARCH_CODE_EMPTY = 0x2;
    public static final int SEARCH_CODE_NOT_FOUND = 0x3;
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
                    mHintTextView.setText(ResourceUtil.getString(
                            SearchActivity.this,
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
    private ViewFader mViewFader = new ViewFader();
    private Handler mFinishHandler = new Handler();
    private Runnable mFinishRunnable = this::finish;

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

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void exitTransitionWithAction(final Runnable endingAction) {
        Transition transition = FadeOutTransition.withAction(new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
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
                e -> e.onNext(PackageUtil.getPackageWithDrawableList(
                        SearchActivity.this))
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

    interface SearchCallback {
        void call(int code);
    }

    static class SearchAdapter extends BaseRecyclerViewAdapter<SearchAdapter.SearchViewHolder> {

        private static boolean sLock = false;
        private SearchActivity.SearchCallback mSearchCallback;
        private List<IconBean> mShowList = new ArrayList<>();
        private Map<String, List<String>> mPackageDrawableMap = new HashMap<>();
        private List<String> mAllDrawableList = new ArrayList<>();

        private Activity mActivity;
        private boolean mLessAnim = false;
        private boolean mCustomPicker = false;
        private Disposable mDisposable;

        SearchAdapter(Activity activity) {
            super();
            mActivity = activity;

            mPrefs.lessAnim().asObservable()
                    .subscribe(lessAnim -> mLessAnim = lessAnim);

            Observable.just(activity.getResources().getStringArray(R.array.icon_pack))
                    .map(Arrays::asList)
                    .map(list -> Stream.of(list)
                            .filter(value -> !value.startsWith("**"))
                            .collect(Collectors.toList()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        Timber.d(Arrays.toString(list.toArray()).replaceAll(",", "\n"));
                        mAllDrawableList = list;
                    }, Timber::e);
        }

        void setSearchCallback(SearchActivity.SearchCallback callback) {
            mSearchCallback = callback;
        }

        void setPackageDrawableMap(Map<String, List<String>> map) {
            mPackageDrawableMap = map;
        }

        @Override
        public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SearchAdapter.SearchViewHolder(
                    LayoutInflater.from(mActivity)
                            .inflate(R.layout.item_icon_center, parent, false));
        }

        @Override
        public void onBindViewHolder(final SearchViewHolder holder, int position) {
            if (position < mShowList.size()) {
                RequestBuilder<Drawable> request = Glide.with(mActivity)
                        .load(mShowList.get(position).getRes());
                if (mLessAnim) {
                    request.apply(RequestOptions.noAnimation());
                } else {
                    request.transition(new DrawableTransitionOptions().crossFade());
                }
                request.into(holder.icon);

                RxView.clicks(holder.itemView)
                        .filter(click -> !sLock)
                        .subscribe(click -> {
                            sLock = true;
                            new Handler().postDelayed(() -> sLock = false, 1000);
                            if (mCustomPicker) {
                                holder.itemView.setOnClickListener(view -> {
                                    mActivity.setResult(Activity.RESULT_OK,
                                            new Intent().putExtra("icon res",
                                                    mShowList.get(holder.getAdapterPosition())
                                                            .getRes()));
                                    mActivity.finish();
                                });
                            } else {
                                showIconDialog(
                                        holder.icon,
                                        mShowList.get(holder.getAdapterPosition()));
                            }
                        }, Timber::e);
            }
        }

        @Override
        public int getItemCount() {
            return mShowList.size();
        }

        private void showIconDialog(ImageView icon, IconBean iconBean) {
            mActivity.getWindow().getDecorView().post(() -> KeyboardUtil.hideKeyboard(mActivity));
            Intent intent = new Intent(mActivity, IconDialogActivity.class);
            intent.putExtra(IconDialogActivity.EXTRA_RES, iconBean.getRes());
            intent.putExtra(IconDialogActivity.EXTRA_NAME, iconBean.getName());
            intent.putExtra(IconDialogActivity.EXTRA_LABEL, iconBean.getLabel());
            intent.putExtra(IconDialogActivity.EXTRA_LESS_ANIM, mLessAnim);

            if (mLessAnim) {
                mActivity.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            } else {
                mActivity.startActivityForResult(
                        intent,
                        MainActivity.Companion.getREQUEST_ICON_DIALOG(),
                        ActivityOptions.makeSceneTransitionAnimation(
                                mActivity,
                                icon,
                                "icon"
                        ).toBundle()
                );
            }
        }

        void setCustomPicker(boolean customPicker) {
            mCustomPicker = customPicker;
        }

        @SuppressLint("SetTextI18n")
        public void search(final String searchText) {
            Timber.d("text: %s", searchText);
            if (mDisposable != null) {
                mDisposable.dispose();
            }
            if (searchText == null || searchText.isEmpty()) {
                mShowList.clear();
                notifyDataSetChanged();
                if (mSearchCallback != null) {
                    mSearchCallback.call(SearchActivity.SEARCH_CODE_EMPTY);
                }
                return;
            }
            mDisposable = AppSearchResultGetter.searchViaApi(searchText)
                    .map(strings -> {
                        Timber.d(Arrays.toString(strings.toArray()).replace(",", "\n"));
                        LinkedHashSet<String> drawableSet = new LinkedHashSet<>();
                        Stream.of(strings)
                                .filter(packageName -> mPackageDrawableMap.containsKey(packageName))
                                .forEach(packageName -> {
                                    List<String> drawableList =
                                            mPackageDrawableMap.get(packageName);
                                    Stream.of(drawableList).forEach(drawable ->
                                            drawableSet.addAll(Stream.of(mAllDrawableList)
                                                    .filter(s -> s.contains(drawable))
                                                    .collect(Collectors.toList())));
                                    drawableSet.addAll(drawableList);
                                });
                        Timber.d(Arrays.toString(drawableSet.toArray()).replace(",", "\n"));
                        return drawableSet;
                    })
                    .map(set -> {
                        if (!searchText.isEmpty()) {
                            set.addAll(Stream.of(mAllDrawableList)
                                    .filter(s -> s.contains(searchText))
                                    .collect(Collectors.toList()));
                        }
                        return set;
                    })
                    .flatMap(Observable::fromIterable)
                    .map(name -> IconBean.fromDrawableName(mActivity, name))
                    .toList()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(iconBeen -> {
                        if (mSearchCallback != null) {
                            if (iconBeen.size() == 0) {
                                mSearchCallback.call(
                                        SearchActivity.SEARCH_CODE_NOT_FOUND);
                            } else {
                                mSearchCallback.call(
                                        SearchActivity.SEARCH_CODE_OK);
                            }
                        }
                        mShowList = iconBeen;
                        notifyDataSetChanged();
                    }, Timber::e);
        }

        static class SearchViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.frameLayout_item_icon_container)
            FrameLayout container;

            @BindView(R.id.imageView_item_icon)
            ImageView icon;

            SearchViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }

    }
}
