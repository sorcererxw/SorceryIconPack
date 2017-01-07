package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.net.spiders.AppSearchResultGetter;
import com.sorcerer.sorcery.iconpack.ui.activities.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.SearchActivity;
import com.sorcerer.sorcery.iconpack.utils.ListUtil;

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

public class WebSearchAdapter extends RecyclerView.Adapter<WebSearchAdapter.SearchViewHolder> {

    private SearchActivity.SearchCallback mSearchCallback;

    public void setSearchCallback(SearchActivity.SearchCallback callback) {
        mSearchCallback = callback;
    }

    private List<IconBean> mShowList = new ArrayList<>();
    private Map<String, List<String>> mPackageDrawableMap = new HashMap<>();
    private List<String> mAllDrawableList = new ArrayList<>();

    private Activity mActivity;

    public WebSearchAdapter(Activity activity) {
        mActivity = activity;
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

    public void setPackageDrawableMap(Map<String, List<String>> map) {
        mPackageDrawableMap = map;
    }

    @Override
    public WebSearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WebSearchAdapter.SearchViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.item_icon_center, parent, false));
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        if (position < mShowList.size()) {
            Glide.with(mActivity)
                    .load(mShowList.get(position).getRes())
                    .into(holder.icon);

            if (mCustomPicker) {
                holder.itemView.setOnClickListener(view -> {
                    mActivity.setResult(Activity.RESULT_OK,
                            new Intent().putExtra("icon res",
                                    mShowList.get(holder.getAdapterPosition()).getRes()));
                    mActivity.finish();
                });
            } else {
                holder.itemView.setOnClickListener(
                        view -> showIconDialog(holder.icon,
                                mShowList.get(holder.getAdapterPosition())));
            }
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
        Intent intent = new Intent(mActivity, IconDialogActivity.class);
        intent.putExtra(IconDialogActivity.EXTRA_RES, iconBean.getRes());
        intent.putExtra(IconDialogActivity.EXTRA_NAME, iconBean.getName());
        intent.putExtra(IconDialogActivity.EXTRA_LABEL, iconBean.getLabel());
        if (Build.VERSION.SDK_INT >= 21) {
            mActivity.startActivityForResult(
                    intent,
                    MainActivity.REQUEST_ICON_DIALOG,
                    ActivityOptions.makeSceneTransitionAnimation(
                            mActivity,
                            icon,
                            "icon"
                    ).toBundle()
            );
        } else {
            mActivity.startActivityForResult(intent, MainActivity.REQUEST_ICON_DIALOG);
            mActivity.overridePendingTransition(R.anim.fast_fade_in, 0);
        }
    }

    private boolean mCustomPicker = false;

    public void setCustomPicker(boolean customPicker) {
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
        mDisposable = AppSearchResultGetter.search(searchText)
                .map(strings -> {
                    Timber.d(Arrays.toString(strings.toArray()).replace(",", "\n"));
                    LinkedHashSet<String> drawableSet = new LinkedHashSet<>();
                    Stream.of(strings)
                            .filter(packageName -> mPackageDrawableMap.containsKey(packageName))
                            .forEach(packageName -> {
                                List<String> drawableList = mPackageDrawableMap.get(packageName);
                                for (String drawable : drawableList) {
                                    drawableSet.addAll(ListUtil.containsItems(
                                            mAllDrawableList, drawable));
                                }
                                drawableSet.addAll(drawableList);
                            });
                    Timber.d(Arrays.toString(drawableSet.toArray()).replace(",", "\n"));
                    return drawableSet;
                })
                .map(set -> {
                    if (!searchText.isEmpty()) {
                        set.addAll(ListUtil.containsItems(mAllDrawableList, searchText));
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