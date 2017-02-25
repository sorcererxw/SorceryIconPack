package com.sorcerer.sorcery.iconpack.apply.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.apply.ApplyActivity;
import com.sorcerer.sorcery.iconpack.apply.LauncherInfo;
import com.sorcerer.sorcery.iconpack.apply.appliers.api.LauncherApplier;
import com.sorcerer.sorcery.iconpack.apply.appliers.database.googleNow.GoogleNowApplyActivity;
import com.sorcerer.sorcery.iconpack.apply.appliers.database.pixel.PixelApplyActivity;
import com.sorcerer.sorcery.iconpack.apply.appliers.database.smartisan.SmartisanApplyActivity;
import com.sorcerer.sorcery.iconpack.apply.appliers.xposed.XposedApplyFragment;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.ApplyAdapter;
import com.sorcerer.sorcery.iconpack.ui.views.TransparentStatusBarBottomSheetDialog;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/24
 */

public class OverviewFragment extends Fragment {
    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    @BindView(R.id.linearLayout_apply_commend_container)
    LinearLayout mRecommendLayout;

    @BindViews({
                       R.id.recyclerView_apply_recommend,
                       R.id.recyclerView_apply_system,
                       R.id.recyclerView_apply_launcher
               })
    List<RecyclerView> mRecyclerViewList;

    private ApplyAdapter[] mApplyAdapters = new ApplyAdapter[3];
    private GridLayoutManager[] mGridLayoutManagers = new GridLayoutManager[3];

    private ApplyAdapter.OnApplyItemClickListener
            mOnApplyItemClickListener = new ApplyAdapter.OnApplyItemClickListener() {
        @Override
        public void click(LauncherInfo item) {
            if (!item.isInstalled()) {
                onItemClickNotInstalled(item);
            } else {
                onItemClickInstalled(item);
            }
        }

        @Override
        public void longClick(LauncherInfo item) {
            onItemLongClick(item);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_overview, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getApplyActivity().setTitle(R.string.nav_item_apply);
    }

    private void init() {
        RequestManager requestManager = Glide.with(this);

        List<LauncherInfo> systemInfos =
                PackageUtil.generateLauncherInfo(getContext(), R.array.apply_systems);
        List<LauncherInfo> launcherInfos =
                PackageUtil.generateLauncherInfo(getContext(), R.array.apply_launchers);

        List<LauncherInfo> recommendInfos = generateRecommend(systemInfos, launcherInfos);
        Collections.sort(systemInfos, LauncherInfo::compareTo);
        Collections.sort(launcherInfos, LauncherInfo::compareTo);

        if (recommendInfos.isEmpty()) {
            mRecommendLayout.setVisibility(View.GONE);
        }

        mApplyAdapters[0] = new ApplyAdapter(getContext(), recommendInfos, requestManager);
        mApplyAdapters[1] = new ApplyAdapter(getContext(), systemInfos, requestManager);
        mApplyAdapters[2] = new ApplyAdapter(getContext(), launcherInfos, requestManager);

        int span = calSpan(getActivity());
        Stream.range(0, 3).forEach(i -> {
            mApplyAdapters[i].setOnApplyItemClickListener(mOnApplyItemClickListener);
            mGridLayoutManagers[i] = new GridLayoutManager(
                    getContext(), span, LinearLayoutManager.VERTICAL, false);
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
        Timber.d(PackageUtil.getCurrentLauncher(getContext()));
        list.addAll(Stream.of(launcherInfos).filter(value -> value.isInstalled()
                && value.getPackageName().equals(PackageUtil.getCurrentLauncher(getContext())))
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

    private void onItemClickInstalled(LauncherInfo item) {
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
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://zhuti.xiaomi.com/detail/db2b63b2-b14e-4f5f-96bf-c12a730d3fd6"));
            startActivity(intent);
        } else if (item.getLabel().toLowerCase().equals("xposed")) {
            if (Build.VERSION.SDK_INT <= 23) {
                getApplyActivity().addFragment(XposedApplyFragment.newInstance());
            }
        } else if (item.getLabel().toLowerCase().equals("pixel")) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.apply_by_database_declaration_title)
                    .content(ResourceUtil
                            .getString(getContext(),
                                    R.string.apply_by_database_declaration_content)
                            .replaceAll("\\{\\}", "Pixel")
                            .replaceAll("\\|", "\n"))
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onPositive((dialog, which) -> PixelApplyActivity.apply(getContext(), true))
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .build().show();
        } else if (item.getLabel().toLowerCase().equals("smartisan")) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.apply_by_database_declaration_title)
                    .content(ResourceUtil
                            .getString(getContext(),
                                    R.string.apply_by_database_declaration_content)
                            .replaceAll("\\{\\}", "Smartisan")
                            .replaceAll("\\|", "\n"))
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onPositive((dialog, which) -> SmartisanApplyActivity.apply(getContext(), true))
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .build().show();
        } else if (item.getLabel().toLowerCase().equals("google now")) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.apply_by_database_declaration_title)
                    .content(ResourceUtil
                            .getString(getContext(),
                                    R.string.apply_by_database_declaration_content)
                            .replaceAll("\\{\\}", "Google Now")
                            .replaceAll("\\|", "\n"))
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onPositive((dialog, which) -> GoogleNowApplyActivity.apply(getContext(), true))
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .build().show();
        } else {
            new LauncherApplier(getContext(), item.getLabel());
        }
    }

    private void onItemClickNotInstalled(LauncherInfo item) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        if (item.getLabel().toLowerCase().equals("flyme")) {
            builder.content(R.string.apply_tip_only_flyme)
                    .positiveText(R.string.ok);
        } else if (item.getLabel().toLowerCase().equals("miui")) {
            builder.content(R.string.apply_tip_only_miui).positiveText(R.string.ok);
        } else if (item.getLabel().toLowerCase().equals("xposed")) {
            builder.content(R.string.apply_by_xposed_no_xposed).positiveText(R.string.ok);
        } else {
            builder.content(R.string.apply_tip_no_launcher)
                    .positiveText(R.string.ok).negativeText(R.string.cancel)
                    .onPositive((dialog, which) -> {
                        String appPackageName = item.getPackageName();
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
    }

    private void onItemLongClick(LauncherInfo item) {
        if (item.isInstalled() && item.getLabel().toLowerCase().equals("pixel")) {
            TransparentStatusBarBottomSheetDialog dialog =
                    new TransparentStatusBarBottomSheetDialog(getActivity());
            MenuSheetView menuSheetView = new MenuSheetView(getContext(),
                    MenuSheetView.MenuType.LIST,
                    ResourceUtil.getString(getContext(), R.string.options), menuItem -> {
                if (menuItem.getItemId() == R.id.action_restore) {
                    PixelApplyActivity.apply(getContext(), false);
                    new Handler(Looper.getMainLooper()).post(dialog::dismiss);
                } else if (menuItem.getItemId() == R.id.action_apply_icons) {
                    PixelApplyActivity.apply(getContext(), true);
                    new Handler(Looper.getMainLooper()).post(dialog::dismiss);
                }
                return false;
            });
            menuSheetView.inflateMenu(R.menu.menu_apply_pixel);
            dialog.setContentView(menuSheetView);
            dialog.show();
        } else if (item.isInstalled() && item.getLabel().toLowerCase().equals("smartisan")) {
            TransparentStatusBarBottomSheetDialog dialog =
                    new TransparentStatusBarBottomSheetDialog(getActivity());
            MenuSheetView menuSheetView = new MenuSheetView(getContext(),
                    MenuSheetView.MenuType.LIST,
                    ResourceUtil.getString(getContext(), R.string.options), menuItem -> {
                if (menuItem.getItemId() == R.id.action_restore) {
                    SmartisanApplyActivity.apply(getContext(), false);
                } else if (menuItem.getItemId() == R.id.action_apply_icons) {
                    SmartisanApplyActivity.apply(getContext(), true);
                }
                new Handler(Looper.getMainLooper()).post(dialog::dismiss);
                return false;
            });
            menuSheetView.inflateMenu(R.menu.menu_apply_database);
            dialog.setContentView(menuSheetView);
            dialog.show();
        } else if (item.isInstalled() && item.getLabel().toLowerCase().equals("google now")) {
            TransparentStatusBarBottomSheetDialog dialog =
                    new TransparentStatusBarBottomSheetDialog(getActivity());
            MenuSheetView menuSheetView = new MenuSheetView(getContext(),
                    MenuSheetView.MenuType.LIST,
                    ResourceUtil.getString(getContext(), R.string.options), menuItem -> {
                if (menuItem.getItemId() == R.id.action_restore) {
                    GoogleNowApplyActivity.apply(getContext(), false);
                } else if (menuItem.getItemId() == R.id.action_apply_icons) {
                    GoogleNowApplyActivity.apply(getContext(), true);
                }
                new Handler(Looper.getMainLooper()).post(dialog::dismiss);
                return false;
            });
            menuSheetView.inflateMenu(R.menu.menu_apply_database);
            dialog.setContentView(menuSheetView);
            dialog.show();
        } else {
            mOnApplyItemClickListener.click(item);
        }
    }

    private ApplyActivity getApplyActivity() {
        return (ApplyActivity) getActivity();
    }
}
