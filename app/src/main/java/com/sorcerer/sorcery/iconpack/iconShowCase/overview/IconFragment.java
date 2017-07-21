package com.sorcerer.sorcery.iconpack.iconShowCase.overview;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mikepenz.materialize.util.UIUtils;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.AppfilterItem;
import com.sorcerer.sorcery.iconpack.data.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.fragments.LazyFragment;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;
import com.turingtechnologies.materialscrollbar.Indicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/10
 */
public class IconFragment extends LazyFragment implements IViewPageFragment {

    private static final String mArgFlagKey = "flag";
    private static final String mArgCustomPickerKey = "custom picker";
    @BindView(R.id.relativeLayout_icon_gird)
    RelativeLayout mRoot;
    @BindView(R.id.recyclerView_icon_gird)
    RecyclerView mGridView;
    private List<IconBean> mIconBeanList;
    private DragScrollBar mScrollBar;
    private IconAdapter mIconAdapter;
    private GridLayoutManager mGridLayoutManager;
    private boolean mNeedResize = false;
    private int mNumOfRows;

    public static IconFragment newInstance(IconFlag flag, boolean customPicker) {
        IconFragment fragment = new IconFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(mArgFlagKey, flag);
        bundle.putBoolean(mArgCustomPickerKey, customPicker);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onStateChange(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (mScrollBar != null) {
                mScrollBar.animate().alpha(1).start();
            }
        } else {
            if (mScrollBar != null) {
                mScrollBar.animate().alpha(0).start();
            }
        }
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstance) {

        View view = View.inflate(getContext(), R.layout.fragment_icon, null);
        ButterKnife.bind(this, view);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int h_screen = dm.heightPixels;
        mGridView.setTranslationY(h_screen);
        mGridView.setAlpha(0);

        setContentView(view);
        if (mIconBeanList == null) {
            getIconBeanList((IconFlag) getArguments().get(mArgFlagKey));
        } else {
            init();
        }
    }

    private void init() {
        calcNumOfRows();

        mGridLayoutManager = new GridLayoutManager(getContext(), mNumOfRows);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mIconAdapter.isItemHead(position) || mIconAdapter.isItemFooter(position)) ?
                        mNumOfRows : 1;
            }
        });
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mGridLayoutManager.scrollToPosition(0);

        final RequestManager rm = Glide.with(this);
        mIconAdapter = new IconAdapter(getActivity(), getContext(), mIconBeanList, mNumOfRows, rm,
                mGridView, mGridLayoutManager);

        boolean customPicker = getArguments().getBoolean(mArgCustomPickerKey, false);

        mGridView.setLayoutManager(mGridLayoutManager);
        mGridView.setHasFixedSize(true);

        if (customPicker) {
            mIconAdapter.setCustomPicker(getActivity(), true);
        }
        mGridView.setAdapter(mIconAdapter);
        if (mNeedResize) {
            resize();
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int h_screen = dm.heightPixels;
        mGridView.setTranslationY(h_screen);
        mGridView.setAlpha(0);
        mGridView.animate()
                .alpha(1)
                .translationY(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(350)
                .start();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        resize();
    }

    private void resize() {
        if (mIconBeanList != null) {
            mNeedResize = false;
            calcNumOfRows();
            mGridLayoutManager.setSpanCount(mNumOfRows);
            mIconAdapter.setSpan(mNumOfRows);
        } else {
            mNeedResize = true;
        }
    }

    private void calcNumOfRows() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float iconMargin = getResources().getDimension(R.dimen.icon_grid_item_margin);
        float cardMargin = getResources().getDimension(R.dimen.icon_grid_card_margin);
        float iconSize = getResources().getDimension(R.dimen.icon_grid_item_size) + 2 * iconMargin;
        mNumOfRows = (int) ((size.x - 2 * cardMargin - 2 * iconMargin) / iconSize);
    }

    public RecyclerView getRecyclerView() {
        return mGridView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resize();
    }

    private void getIconBeanList(IconFlag flag) {
        Observable.just(flag).map(fg -> Stream.of(getIconNames(getContext(), fg))
                .map(name -> {
                    IconBean iconBean = new IconBean(name);
                    if (name.startsWith("**")) {
                        return iconBean;
                    }
                    int res = getResources().getIdentifier(name, "drawable",
                            getContext().getPackageName());
                    if (res != 0) {
                        iconBean.setRes(res);
                        return iconBean;
                    } else {
                        Timber.e("thumb = 0: %s", name);
                        return null;
                    }
                })
                .filter(value -> value != null)
                .collect(Collectors.toList()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    mIconBeanList = list;
                    init();
                }, Timber::e);
    }

    private List<String> getIconNames(Context context, IconFlag flag) {
        if (flag == IconFlag.INSTALLED) {
            List<String> list = new ArrayList<>();
            List<AppfilterItem> afList = PackageUtil.getAppfilterList(context);
            List<ResolveInfo> installedList = PackageUtil.getInstallApps(context);
            List<String> allIconList =
                    Arrays.asList(ResourceUtil.getStringArray(context, "icon_pack"));
            Collections.sort(allIconList);

            for (ResolveInfo ri : installedList) {
                String comp = ri.activityInfo.packageName + "/" + ri.activityInfo.name;
                for (int i = 0; i < afList.size(); i++) {
                    AppfilterItem ai = afList.get(i);
                    if (comp.equals(ai.getComponent())) {
                        String drawable = ai.getDrawable();
                        list.add(drawable);
                        int index = allIconList.indexOf(drawable);
                        if (index < 0) {
                            break;
                        }
                        for (int j = index + 1; j < allIconList.size(); j++) {
                            if (allIconList.get(j).startsWith(drawable + "_alt") ||
                                    (drawable.endsWith("calendar")
                                            && allIconList.get(j).startsWith(drawable))) {
                                list.add(allIconList.get(j));
                            } else {
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            Collections.sort(list);
            return list;
        }
        if (flag == IconFlag.ALL) {
            return Arrays.asList(ResourceUtil.getStringArray(context, "icon_pack"));
        } else if (flag == IconFlag.NEW) {
            return Arrays.asList(ResourceUtil.getStringArray(context, "icon_pack_new"));
        }
        return Stream.of(ResourceUtil.getStringArray(context, "icon_pack"))
                .filter(value -> value.toLowerCase().startsWith(flag.toString().toLowerCase()))
                .collect(Collectors.toList());
    }

    private void initScrollerBar() {
        if (getArguments().get(mArgFlagKey) != IconFlag.ALL) {
            return;
        }
        mScrollBar = new DragScrollBar(getContext(), mGridView, true);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        mScrollBar.setLayoutParams(params);
        mScrollBar.setScrollBarSize((int) UIUtils.convertDpToPixel(100, getContext()));
        Indicator indicator = new AlphabetIndicator(getContext());
        mScrollBar.setIndicator(indicator, true);
        mScrollBar.setFitsSystemWindows(true);
        mRoot.addView(mScrollBar, 1);
    }

}