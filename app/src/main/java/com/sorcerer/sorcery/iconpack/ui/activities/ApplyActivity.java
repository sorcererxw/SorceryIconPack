package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.apply.LauncherInfo;
import com.sorcerer.sorcery.iconpack.apply.appliers.api.LauncherApplier;
import com.sorcerer.sorcery.iconpack.apply.appliers.database.googleNow.GoogleNowLauncherApplier;
import com.sorcerer.sorcery.iconpack.apply.appliers.database.pixel.PixelApplyActivity;
import com.sorcerer.sorcery.iconpack.apply.appliers.database.smartisan.SmartisanApplyActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.ApplyAdapter;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.ApplyAdapter.OnApplyItemClickListener;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/2
 */

public class ApplyActivity extends BaseSubActivity {

    @BindView(R.id.linearLayout_apply_commend_container)
    LinearLayout mRecommendLayout;

    @BindViews({
                       R.id.recyclerView_apply_recommend,
                       R.id.recyclerView_apply_system,
                       R.id.recyclerView_apply_launcher
               })
    List<RecyclerView> mRecyclerViewList;

    @BindView(R.id.bottomSheetLayout)
    BottomSheetLayout mBottomSheetLayout;

    private ApplyAdapter[] mApplyAdapters = new ApplyAdapter[3];
    private GridLayoutManager[] mGridLayoutManagers = new GridLayoutManager[3];

    private OnApplyItemClickListener mOnApplyItemClickListener = new OnApplyItemClickListener() {
        @Override
        public void click(LauncherInfo item) {
            if (!item.isInstalled()) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
                if (item.getLabel().toLowerCase().equals("flyme")) {
                    builder.content(R.string.apply_tip_only_flyme)
                            .positiveText(R.string.ok);
                } else if (item.getLabel().toLowerCase().equals("miui")) {
                    builder.content(R.string.apply_tip_only_miui).positiveText(R.string.ok);
                } else {
                    builder.content(R.string.apply_tip_no_launcher)
                            .positiveText(R.string.ok).negativeText(R.string.cancel)
                            .onPositive((dialog, which) -> {
                                final String appPackageName = item.getPackageName();
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id="
                                                    + appPackageName)));
                                } catch (android.content.ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                            "https://play.google.com/store/apps/details?id="
                                                    + appPackageName)));
                                }
                            });
                }
                builder.build().show();
                return;
            }
            if (item.getLabel().toLowerCase().equals("flyme")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.meizu.customizecenter",
                        "com.meizu.customizecenter.OnlineThemeActivity"));
                intent.putExtra("ONLINE_THEME_WAY", "ONLINE_WAY_URL");
                intent.putExtra("URL", "/themes/public/detail/3000115");
                intent.putExtra("search_content_type", "themes");
                intent.putExtra("position", 0);
                intent.putExtra("search_action", "click_history_label");
                intent.putExtra("event_path", "OneSearchActivity");
                intent.putExtra("search_content", "Sorcery");
                intent.putExtra("search_id", "cbe31a19-ac0f-4d86-b8a3-2077fc132088");
                startActivity(intent);
            } else if (item.getLabel().toLowerCase().equals("miui")) {

            } else if (item.getLabel().toLowerCase().equals("pixel")) {
                new MaterialDialog.Builder(ApplyActivity.this)
                        .title(R.string.apply_by_database_declaration_title)
                        .content(ResourceUtil
                                .getString(mContext,
                                        R.string.apply_by_database_declaration_content)
                                .replaceAll("\\{\\}", "Pixel")
                                .replaceAll("\\|", "\n"))
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive((dialog, which) -> {
                            PixelApplyActivity.apply(mContext, true);
                        })
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .build().show();
            } else if (item.getLabel().toLowerCase().equals("smartisan")) {
                new MaterialDialog.Builder(ApplyActivity.this)
                        .title(R.string.apply_by_database_declaration_title)
                        .content(ResourceUtil
                                .getString(mContext,
                                        R.string.apply_by_database_declaration_content)
                                .replaceAll("\\{\\}", "Smartisan")
                                .replaceAll("\\|", "\n"))
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive((dialog, which) -> SmartisanApplyActivity.apply(mContext, true))
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .build().show();
            } else if (item.getLabel().toLowerCase().equals("google now")) {
                new GoogleNowLauncherApplier(ApplyActivity.this).apply()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<String>>() {
                            private ProgressDialog mDialog;

                            @Override
                            public void onSubscribe(Disposable d) {
                                mDialog = new ProgressDialog(ApplyActivity.this);
                                mDialog.setMessage("applying");
                                mDialog.show();
                            }

                            @Override
                            public void onNext(List<String> list) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e);
                                mDialog.dismiss();
                                Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onComplete() {
                                mDialog.dismiss();
                                Toast.makeText(mContext, "success!!!\nenjoy it:)",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                mActivity.getWindow().getDecorView().postDelayed(() -> {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.setComponent(
                                            new ComponentName(
                                                    "com.google.android.googlequicksearchbox",
                                                    "com.google.android.launcher.GEL"));
                                    startActivity(intent);
                                }, 500);
                            }
                        });
            } else {
                new LauncherApplier(ApplyActivity.this, item.getLabel());
            }
        }

        @Override
        public void longClick(LauncherInfo item) {
            if (item.isInstalled() && item.getLabel().toLowerCase().equals("pixel")) {
                MenuSheetView menuSheetView = new MenuSheetView(ApplyActivity.this,
                        MenuSheetView.MenuType.LIST,
                        ResourceUtil.getString(mContext, R.string.options), menuItem -> {
                    if (menuItem.getItemId() == R.id.action_restore) {
                        PixelApplyActivity.apply(mContext, false);
                    } else if (menuItem.getItemId() == R.id.action_apply_icons) {
                        PixelApplyActivity.apply(mContext, true);
                    }
                    mBottomSheetLayout.post(() -> mBottomSheetLayout.dismissSheet());
                    return false;
                });
                menuSheetView.inflateMenu(R.menu.menu_apply_database);
                mBottomSheetLayout.setShouldDimContentView(true);
                mBottomSheetLayout.showWithSheetView(menuSheetView);
            }else if(item.isInstalled() && item.getLabel().toLowerCase().equals("smartisan")){
                MenuSheetView menuSheetView = new MenuSheetView(ApplyActivity.this,
                        MenuSheetView.MenuType.LIST,
                        ResourceUtil.getString(mContext, R.string.options), menuItem -> {
                    if (menuItem.getItemId() == R.id.action_restore) {
                        SmartisanApplyActivity.apply(mContext, false);
                    } else if (menuItem.getItemId() == R.id.action_apply_icons) {
                        SmartisanApplyActivity.apply(mContext, true);
                    }
                    mBottomSheetLayout.post(() -> mBottomSheetLayout.dismissSheet());
                    return false;
                });
                menuSheetView.inflateMenu(R.menu.menu_apply_database);
                mBottomSheetLayout.setShouldDimContentView(true);
                mBottomSheetLayout.showWithSheetView(menuSheetView);
            }
            else {
                click(item);
            }
        }
    };

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_apply;
    }

    @Override
    protected void init() {
        super.init();

        setToolbarBackIndicator();

        RequestManager requestManager = Glide.with(this);

        List<LauncherInfo> systemInfos =
                PackageUtil.generateLauncherInfo(this, R.array.apply_systems);
        List<LauncherInfo> launcherInfos =
                PackageUtil.generateLauncherInfo(this, R.array.apply_launchers);

        List<LauncherInfo> recommendInfos = generateRecommend(systemInfos, launcherInfos);
        Collections.sort(systemInfos, LauncherInfo::compareTo);
        Collections.sort(launcherInfos, LauncherInfo::compareTo);

        if (recommendInfos.isEmpty()) {
            mRecommendLayout.setVisibility(View.GONE);
        }

        mApplyAdapters[0] = new ApplyAdapter(this, recommendInfos, requestManager);
        mApplyAdapters[1] = new ApplyAdapter(this, systemInfos, requestManager);
        mApplyAdapters[2] = new ApplyAdapter(this, launcherInfos, requestManager);

        int span = calSpan(this);
        Stream.range(0, 3).forEach(i -> {
            mApplyAdapters[i].setOnApplyItemClickListener(mOnApplyItemClickListener);
            mGridLayoutManagers[i] = new GridLayoutManager(
                    ApplyActivity.this, span, LinearLayoutManager.VERTICAL, false);
            mRecyclerViewList.get(i).setLayoutManager(mGridLayoutManagers[i]);
            mRecyclerViewList.get(i).setAdapter(mApplyAdapters[i]);
            mRecyclerViewList.get(i).setHasFixedSize(false);
            mRecyclerViewList.get(i).setNestedScrollingEnabled(false);
        });
    }

    private List<LauncherInfo> generateRecommend(List<LauncherInfo> systemInfos,
                                                 List<LauncherInfo> launcherInfos) {
        List<LauncherInfo> list = new ArrayList<>();

        for (int i = 0; i < systemInfos.size(); i++) {
            if (systemInfos.get(i).isInstalled()) {
                list.add(systemInfos.get(i));
                if (systemInfos.get(i).getLabel().toLowerCase().equals("flyme")
                        || systemInfos.get(i).getLabel().toLowerCase().equals("miui")) {
                    return list;
                }
            }
        }

        list.addAll(Stream.of(launcherInfos)
                .filter(value ->
                        value.isInstalled() && value.getPackageName()
                                .equals(PackageUtil.getCurrentLauncher(mContext)))
                .collect(Collectors.toList()));
        return list;
    }

    private static int calSpan(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float itemMargin = activity.getResources().getDimension(R.dimen.apply_icon_margin);
        float cardMargin = activity.getResources().getDimension(R.dimen.apply_card_margin);
        float iconSize =
                activity.getResources().getDimension(R.dimen.apply_icon_size) + 2 * itemMargin;
        return (int) ((size.x - 2 * cardMargin) / iconSize);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }
}
