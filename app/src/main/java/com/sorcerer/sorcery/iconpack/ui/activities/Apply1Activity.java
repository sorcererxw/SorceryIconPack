package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.Apply1Adapter;
import com.sorcerer.sorcery.iconpack.utils.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.utils.LauncherIntents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/2
 */

public class Apply1Activity extends SlideInAndOutAppCompatActivity {

    @BindView(R.id.linearLayout_apply_commend_container)
    LinearLayout mRecommendLayout;

    @BindViews({R.id.recyclerView_apply_recommend,
                       R.id.recyclerView_apply_system,
                       R.id.recyclerView_apply_launcher})
    List<RecyclerView> mRecyclerViewList;

    private Apply1Adapter[] mApply1Adapters = new Apply1Adapter[3];
    private GridLayoutManager[] mGridLayoutManagers = new GridLayoutManager[3];

    private Apply1Adapter.OnApplyItemClickListener mOnApplyItemClickListener =
            new Apply1Adapter.OnApplyItemClickListener() {
                @Override
                public void click(final LauncherInfo item) {
                    if (!item.isInstalled()) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
                        if (item.getLabel().toLowerCase().equals("flyme")) {
                            builder.content("仅限Flyme系统可以使用.")
                                    .positiveText(R.string.ok);
                        } else if (item.getLabel().toLowerCase().equals("miui")) {
                            builder.content("MIUI主题尚未上线, 请稍作等待.")
                                    .positiveText(R.string.ok);
                        } else {
                            builder.content("尚未安装启动器, 是否前往商店安装?")
                                    .positiveText(R.string.ok).negativeText(R.string.cancel)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog,
                                                            @NonNull DialogAction which) {
                                            final String appPackageName = item.getPackageName();
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("market://details?id="
                                                                + appPackageName)));
                                            } catch (android.content.ActivityNotFoundException e) {
                                                startActivity(new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse(
                                                                "https://play.google.com/store/apps/details?id="
                                                                        + appPackageName)));
                                            }
                                        }
                                    });
                        }
                        builder.build().show();
                        return;
                    }
                    if (item.getLabel().toLowerCase().equals("flyme")) {
                        Intent intent = new Intent();
                        intent.setComponent(
                                new ComponentName("com.meizu.customizecenter",
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

                    } else {
                        new LauncherIntents(Apply1Activity.this, item.getLabel());
                    }
                }
            };

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_apply_1;
    }

    @Override
    protected void init() {
        super.init();

        setToolbarBackIndicator();

        RequestManager requestManager = Glide.with(this);

        List<LauncherInfo> systemInfos =
                AppInfoUtil.generateLauncherInfo(this, R.array.apply_systems);
        List<LauncherInfo> launcherInfos =
                AppInfoUtil.generateLauncherInfo(this, R.array.apply_launchers);
        List<LauncherInfo> recommendInfos = generateRecommend(systemInfos, launcherInfos);
        Collections.sort(systemInfos);
        Collections.sort(launcherInfos);

        if (recommendInfos.isEmpty()) {
            mRecommendLayout.setVisibility(View.GONE);
        }

        mApply1Adapters[0] = new Apply1Adapter(this, recommendInfos, requestManager);
        mApply1Adapters[1] = new Apply1Adapter(this, systemInfos, requestManager);
        mApply1Adapters[2] = new Apply1Adapter(this, launcherInfos, requestManager);

        int span = calSpan(this);
        for (int i = 0; i < 3; i++) {
            mApply1Adapters[i].setOnApplyItemClickListener(mOnApplyItemClickListener);
            mGridLayoutManagers[i] =
                    new GridLayoutManager(this, span, LinearLayoutManager.VERTICAL, false);
            mRecyclerViewList.get(i).setLayoutManager(mGridLayoutManagers[i]);
            mRecyclerViewList.get(i).setAdapter(mApply1Adapters[i]);
            mRecyclerViewList.get(i).setHasFixedSize(false);
            mRecyclerViewList.get(i).setNestedScrollingEnabled(false);
        }
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
        PackageManager localPackageManager = getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        String currentLauncher = localPackageManager.resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
        for (int i = 0; i < launcherInfos.size(); i++) {
            if (launcherInfos.get(i).isInstalled()
                    && launcherInfos.get(i).getPackageName().equals(currentLauncher)) {
                list.add(launcherInfos.get(i));
            }
        }
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
