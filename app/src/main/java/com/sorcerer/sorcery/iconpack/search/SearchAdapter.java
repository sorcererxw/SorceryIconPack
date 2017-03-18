package com.sorcerer.sorcery.iconpack.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.mikepenz.materialize.util.KeyboardUtil;
import com.sorcerer.sorcery.iconpack.MainActivity;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.IconBean;
import com.sorcerer.sorcery.iconpack.iconShowCase.detail.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.network.spiders.AppSearchResultGetter;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/6
 */

class SearchAdapter extends BaseRecyclerViewAdapter<SearchAdapter.SearchViewHolder> {

    private SearchActivity.SearchCallback mSearchCallback;

    void setSearchCallback(SearchActivity.SearchCallback callback) {
        mSearchCallback = callback;
    }

    private List<IconBean> mShowList = new ArrayList<>();
    private Map<String, List<String>> mPackageDrawableMap = new HashMap<>();
    private List<String> mAllDrawableList = new ArrayList<>();

    private Activity mActivity;
    private boolean mLessAnim = false;

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

    void setPackageDrawableMap(Map<String, List<String>> map) {
        mPackageDrawableMap = map;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchAdapter.SearchViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.item_icon_center, parent, false));
    }

    private static boolean sLock = false;

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        if (position < mShowList.size()) {
            DrawableTypeRequest<Integer> request = Glide.with(mActivity)
                    .load(mShowList.get(position).getRes());
            if (mLessAnim) {
                request.dontAnimate();
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
                    MainActivity.REQUEST_ICON_DIALOG,
                    ActivityOptions.makeSceneTransitionAnimation(
                            mActivity,
                            icon,
                            "icon"
                    ).toBundle()
            );
        }
    }

    private boolean mCustomPicker = false;

    void setCustomPicker(boolean customPicker) {
        mCustomPicker = customPicker;
    }

    private Disposable mDisposable;

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
                                List<String> drawableList = mPackageDrawableMap.get(packageName);
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
                .flatMap(new Function<Set<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Set<String> strings) throws Exception {
                        return Observable.fromIterable(strings);
                    }
                })
                .map(name -> IconBean.fromDrawableName(mActivity, name))
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(iconBeen -> {
                    if (mSearchCallback != null) {
                        if (iconBeen.size() == 0) {
                            mSearchCallback.call(SearchActivity.SEARCH_CODE_NOT_FOUND);
                        } else {
                            mSearchCallback.call(SearchActivity.SEARCH_CODE_OK);
                        }
                    }
                    mShowList = iconBeen;
                    notifyDataSetChanged();
                }, Timber::e);
    }

}