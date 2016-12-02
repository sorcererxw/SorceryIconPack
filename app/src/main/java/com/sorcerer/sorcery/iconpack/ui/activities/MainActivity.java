package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.PermissionBean;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.ViewPageAdapter;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.PermissionAdapter;
import com.sorcerer.sorcery.iconpack.ui.fragments.LazyIconFragment;
import com.sorcerer.sorcery.iconpack.ui.others.SearchTransitioner;
import com.sorcerer.sorcery.iconpack.ui.others.ViewFader;
import com.sorcerer.sorcery.iconpack.ui.views.DoubleTapTabLayout;
import com.sorcerer.sorcery.iconpack.ui.views.ExposedSearchToolbar;
import com.sorcerer.sorcery.iconpack.utils.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.utils.DisplayUtil;
import com.sorcerer.sorcery.iconpack.ui.Navigator;
import com.sorcerer.sorcery.iconpack.utils.OtherUtil;
import com.sorcerer.sorcery.iconpack.utils.Prefs.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;
import timber.log.Timber;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Sorcerer on 2016/6/1 0001.
 * <p/>
 * MainActivity
 * The first activity with drawer and icon viewpager.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.exposedSearchToolbar)
    ExposedSearchToolbar mSearchToolbar;

    @BindView(R.id.linearLayout_content_main)
    LinearLayout mContent;

    @BindView(R.id.appBarLayout_main)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.viewPager_icon)
    ViewPager mViewPager;

    @BindView(R.id.tabLayout_icon)
    DoubleTapTabLayout mTabLayout;

    @BindView(R.id.coordinatorLayout_main)
    CoordinatorLayout mCoordinatorLayout;

    private Drawer mDrawer;

    private static final String TAG = "TAG_MAIN_ACTIVITY";

    public static final int REQUEST_ICON_DIALOG = 100;

    public static Intent mLaunchIntent;
    private ViewPageAdapter mPageAdapter;
    private boolean mCustomPicker = false;
    private ViewPager.OnPageChangeListener mPageChangeListener =
            new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                    if (position == 0 && positionOffsetPixels == 0) {
                        times++;
                        if (times >= 3) {
                            openDrawer();
                        }
                    }
                }

                int times = 0;

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    times = 0;
                }
            };
    private Navigator mNavigator;
    private SearchTransitioner mSearchTransitioner;
    private SorceryPrefs mPrefs;

    private static final boolean ENABLE_GUIDE = false;

    @Override
    protected void hookBeforeSetContentView() {
        super.hookBeforeSetContentView();
        mPrefs = SorceryPrefs.getInstance(this);
        if (ENABLE_GUIDE && !mPrefs.userGuideShowed().getValue()) {
            startActivity(new Intent(this, WelcomeActivity.class));
            overridePendingTransition(0, 0);
            this.finish();
        }
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        Log.d(TAG, "onCreate");
        getWindow().setBackgroundDrawable(null);

        mLaunchIntent = getIntent();
        String action = getIntent().getAction();

        mCustomPicker = "com.novalauncher.THEME".equals(action);

        setSupportActionBar(mSearchToolbar);

        mNavigator = new Navigator(this);

        initTabAndPager();

        initDrawer();

        mSearchTransitioner = new SearchTransitioner(this,
                new Navigator(this),
                mTabLayout,
                mViewPager,
                mSearchToolbar,
                new ViewFader());

        mSearchToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppBarLayout.setExpanded(true, true);
                mAppBarLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSearchTransitioner.transitionToSearch();
                    }
                });
            }
        });
        mSearchToolbar.setTitle("Sorcery Icons");

        if (!mCustomPicker) {
            showPermissionDialog();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setTitle(getString(R.string.select_an_icon));
            }
        }
    }

    private void showPermissionDialog() {
        if (Build.VERSION.SDK_INT >= 23) {
            final RxPermissions rxPermissions = RxPermissions.getInstance(this);
            if (!rxPermissions.isGranted(READ_PHONE_STATE)
                    || !rxPermissions.isGranted(WRITE_EXTERNAL_STORAGE)) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
                builder.title(ResourceUtil.getString(this, R.string.permission_request_title));
                List<PermissionBean> list = new ArrayList<>();
                list.add(new PermissionBean(null,
                        ResourceUtil.getString(this, R.string.permission_request_content), 0));
                if (!rxPermissions.isGranted(READ_PHONE_STATE)) {
                    list.add(new PermissionBean(
                            ResourceUtil.getString(this, R.string.permission_read_phone_state),
                            ResourceUtil.getString(this,
                                    R.string.permission_request_read_phone_state),
                            R.drawable.ic_smartphone_black_24dp));
                }
                if (!rxPermissions.isGranted(WRITE_EXTERNAL_STORAGE)) {
                    list.add(new PermissionBean(
                            ResourceUtil.getString(this,
                                    R.string.permission_write_external_storage),
                            ResourceUtil.getString(this,
                                    R.string.permission_request_describe_write_external_storage),
                            R.drawable.ic_folder_black_24dp));
                }
                builder.adapter(new PermissionAdapter(this, list),
                        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(
                            @NonNull
                                    MaterialDialog materialDialog,
                            @NonNull
                                    DialogAction dialogAction) {
                        rxPermissions.request(READ_PHONE_STATE,
                                WRITE_EXTERNAL_STORAGE)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean aBoolean) {
                                        if (!aBoolean) {
                                            showPermissionDialog();
                                        }
                                    }
                                });
                        materialDialog.dismiss();
                    }
                });
                builder.positiveText(
                        ResourceUtil.getString(this, R.string.action_grant_permission));
                builder.negativeText(ResourceUtil.getString(this, R.string.action_refuse));
                builder.canceledOnTouchOutside(false);
                builder.build().show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchTransitioner.onActivityResumed();
            }
        }, 100);
    }

    private void initTabAndPager() {
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setPageMargin(DisplayUtil.dip2px(mContext, 16));

        mPageAdapter = new ViewPageAdapter(this, getSupportFragmentManager(), mCustomPicker);

        mViewPager.setAdapter(mPageAdapter);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setOnTabDoubleTapListener(new DoubleTapTabLayout.OnTabDoubleTapListener() {
            @Override
            public void onDoubleTap() {
                int index = mViewPager.getCurrentItem();
                ViewPageAdapter adapter = (ViewPageAdapter) mViewPager.getAdapter();
                LazyIconFragment fragment = (LazyIconFragment) adapter.getItem(index);
                if (fragment.getRecyclerView() != null) {
                    fragment.getRecyclerView().smoothScrollToPosition(0);
                }
            }
        });

    }

    private void initDrawer() {
        AccountHeaderBuilder headBuilder = new AccountHeaderBuilder();
        headBuilder.withActivity(this)
                .withHeightDp(178)
                .withProfileImagesClickable(false)
                .withResetDrawerOnProfileListClick(false)
                .withSelectionListEnabled(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withDividerBelowHeader(false)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP);
        if (OtherUtil.showHead(this)) {
            headBuilder.withHeaderBackground(R.drawable.sandy_shore);
        } else {
            headBuilder.withHeaderBackground(R.drawable.sorcery_head);
        }

        int textColorRes = R.color.grey_800;
        int subTextColorRes = R.color.grey_600;
        int iconAlpha = 128;
        mDrawer = new DrawerBuilder()
                .withCloseOnClick(true)
                .withToolbar(mSearchToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View view) {
                        openDrawer();
                        return false;
                    }
                })
                .withAccountHeader(headBuilder.build())
                .withActivity(mActivity)
                .build();
        mDrawer.addItems(
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("apply")
                        .withIcon(ResourceUtil
                                .getDrawableWithAlpha(mContext, R.drawable.ic_input_black_24dp,
                                        iconAlpha))
                        .withTextColorRes(textColorRes)
                        .withName(R.string.nav_item_apply),
                new ExpandableDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext,
                                R.drawable.ic_mail_black_24dp, iconAlpha))
                        .withTextColorRes(textColorRes)
                        .withName(R.string.nav_item_feedback)
                        .withTag("")
                        .withSubItems(
                                new SecondaryDrawerItem()
                                        .withLevel(3)
                                        .withSetSelected(false)
                                        .withSelectable(false)
                                        .withName(R.string.request)
                                        .withTextColorRes(subTextColorRes)
                                        .withTag("request"),
                                new SecondaryDrawerItem()
                                        .withLevel(3)
                                        .withSetSelected(false)
                                        .withSelectable(false)
                                        .withTextColorRes(subTextColorRes)
                                        .withName(R.string.suggest)
                                        .withTag("suggest")
                        ),
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("settings")
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                                .ic_settings_black_24dp, iconAlpha))
                        .withTextColorRes(textColorRes)
                        .withName(R.string.nav_item_settings),
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("help")
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                                .ic_help_black_24dp, iconAlpha))
                        .withTextColorRes(textColorRes)
                        .withName(R.string.nav_item_help)
        );

        if (AppInfoUtil.isAlipayInstalled(this)) {
            mDrawer.addItem(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("donate")
                    .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                            .ic_attach_money_black_24dp, iconAlpha))
                    .withTextColorRes(textColorRes)
                    .withName(R.string.nav_item_donate));
        }

        if (BuildConfig.DEBUG) {
            if (AppInfoUtil.isXposedInstalled(this)) {
                mDrawer.addItem(new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("lab")
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                                .ic_settings_black_24dp, iconAlpha))
                        .withTextColorRes(textColorRes)
                        .withName(R.string.nav_item_lab));
            }
        }

        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch ((String) drawerItem.getTag()) {
                    case "apply":
                        mNavigator.toAppleActivity();
                        break;
                    case "lab":
                        mNavigator.toLabActivity();
                        break;
                    case "settings":
                        mNavigator.toSettingsActivity();
                        break;
                    case "help":
                        mNavigator.toHelpActivity();
                        break;
                    case "donate":
                        mNavigator.toDonateActivity();
                        break;
                    case "request":
                        mNavigator.toIconRequest();
                        break;
                    case "suggest":
                        mNavigator.toFeedbackChatActivity();
                        break;
                }
                return false;
            }
        });

        for (int i = 0; i < 10; i++) {
            mDrawer.addItems(new PrimaryDrawerItem()
                    .withIconColorRes(0)
                    .withName("")
                    .withSelectable(false)
                    .withEnabled(false)
                    .withSetSelected(false));
        }

        final RecyclerView drawerRecyclerView = mDrawer.getRecyclerView();
        drawerRecyclerView.setVerticalScrollBarEnabled(false);
        drawerRecyclerView.setHorizontalScrollBarEnabled(false);
        drawerRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String[] actions = new String[]{
                        "ACTION_BUTTON_PRESS =11",
                        "ACTION_BUTTON_RELEASE =12",
                        "ACTION_CANCEL =3",
                        "ACTION_DOWN =0",
                        "ACTION_HOVER_ENTER =9",
                        "ACTION_HOVER_EXIT =10",
                        "ACTION_HOVER_MOVE =7",
                        "ACTION_MASK =255",
                        "ACTION_MOVE =2",
                        "ACTION_OUTSIDE =4",
                        "ACTION_POINTER_1_DOWN =5",
                        "ACTION_POINTER_1_UP =6",
                        "ACTION_POINTER_2_DOWN =261",
                        "ACTION_POINTER_2_UP =262",
                        "ACTION_POINTER_3_DOWN =517",
                        "ACTION_POINTER_3_UP =518",
                        "ACTION_POINTER_DOWN =5",
                        "ACTION_POINTER_ID_MASK =65280",
                        "ACTION_POINTER_ID_SHIFT =8",
                        "ACTION_POINTER_INDEX_MASK =65280",
                        "ACTION_POINTER_INDEX_SHIFT =8",
                        "ACTION_POINTER_UP =6",
                        "ACTION_SCROLL =8",
                        "ACTION_UP =1"};
                int actionId = motionEvent.getAction();
                for (String action : actions) {
                    if (Integer.valueOf(action.split(" =")[1]) == actionId) {
                        Timber.d(action);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    drawerRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerRecyclerView.smoothScrollToPosition(0);
                        }
                    }, 200);
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed();
        }
    }

    private void openDrawer() {
        if (mDrawer != null) {
            mDrawer.openDrawer();
        }
    }

    private boolean closeDrawer() {
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
            return true;
        }
        return false;
    }

    public boolean isCustomPicker() {
        return mCustomPicker;
    }

    public void onReturnCustomPickerRes(int res) {
        Intent intent = new Intent();
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), res);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        if (bitmap != null) {
            intent.putExtra("icon", bitmap);
            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", res);
            String bmUri =
                    "android.resource://" + mContext.getPackageName() + "/" + String.valueOf(res);
            intent.setData(Uri.parse(bmUri));
            setResult(Activity.RESULT_OK, intent);
        } else {
            setResult(Activity.RESULT_CANCELED, intent);
        }
        finish();
    }
}
