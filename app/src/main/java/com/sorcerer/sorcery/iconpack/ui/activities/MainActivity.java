package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialize.util.UIUtils;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.PermissionBean;
import com.sorcerer.sorcery.iconpack.iconShowCase.overview.IconFragment;
import com.sorcerer.sorcery.iconpack.iconShowCase.overview.IconViewPageAdapter;
import com.sorcerer.sorcery.iconpack.settings.prefs.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.ui.Navigator;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.PermissionAdapter;
import com.sorcerer.sorcery.iconpack.ui.anim.SearchTransitioner;
import com.sorcerer.sorcery.iconpack.ui.anim.ViewFader;
import com.sorcerer.sorcery.iconpack.ui.views.DoubleTapTabLayout;
import com.sorcerer.sorcery.iconpack.ui.views.ExposedSearchToolbar;
import com.sorcerer.sorcery.iconpack.utils.DisplayUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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

    public static final int REQUEST_ICON_DIALOG = 100;

    private IconViewPageAdapter mPageAdapter;
    private boolean mCustomPicker = false;
    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
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
            mPageAdapter.getItem(mViewPager.getCurrentItem()).onSelected();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            times = 0;
            mPageAdapter.getItem(mViewPager.getCurrentItem()).onStateChange(state);
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
        if (!PackageUtil.isInstallFromPlay(this) && BuildConfig.ONLY_FOR_PLAY) {
            Toast.makeText(mContext, "This version can only be installed by google play to use",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

//        new MaterializeBuilder()
//                .withActivity(this)
//                .withStatusBarPadding(true)
//                .build();

//        SorceryPrefs.getInstance(this)
//                .enableTransparentNavBar().asObservable()
//                .subscribe(enable -> {
//                    Window window = getWindow();
//                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                    if (enable) {
//                        window.getDecorView()
//                                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//                    }
//                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    window.setStatusBarColor(Color.TRANSPARENT);
//                    window.setNavigationBarColor(enable ? 0x33000000 : 0xff000000);
//                    new MaterializeBuilder().withActivity(this)
////                            .withStatusBarPadding(true)
//                            .withTransparentStatusBar(true)
//                            .withTranslucentStatusBarProgrammatically(true)
//                            .withTransparentNavigationBar(enable)
//                            .withTranslucentNavigationBarProgrammatically(enable).build();
//                });

        getWindow().setBackgroundDrawable(null);

        mCustomPicker = isCustomPicker(getIntent());

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

        mSearchToolbar.setOnClickListener(view -> {
            mAppBarLayout.setExpanded(true, true);
            mAppBarLayout.post(() -> mSearchTransitioner.transitionToSearch());
        });
        mSearchToolbar.setOnLongClickListener(v -> {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(200);
            mAppBarLayout.setExpanded(true, true);
            mAppBarLayout.post(() -> mSearchTransitioner.transitionToSearch());
            return true;
        });
        mSearchToolbar.setTitle("Sorcery Icons");

        if (mCustomPicker) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setTitle(getString(R.string.select_an_icon));
            }
        }
    }

    private void showPermissionDialog() {
        if (Build.VERSION.SDK_INT >= 23) {
            final RxPermissions rxPermissions = new RxPermissions(this);
            if (!rxPermissions.isGranted(READ_PHONE_STATE)
                    || !rxPermissions.isGranted(WRITE_EXTERNAL_STORAGE)) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
                builder.title(ResourceUtil.getString(this, R.string.permission_request_title));
                List<PermissionBean> list = new ArrayList<>();
                list.add(new PermissionBean(null,
                        ResourceUtil.getString(this, R.string.permission_request_content),
                        null));
                if (!rxPermissions.isGranted(READ_PHONE_STATE)) {
                    list.add(new PermissionBean(
                            ResourceUtil.getString(this, R.string.permission_read_phone_state),
                            ResourceUtil.getString(this,
                                    R.string.permission_request_read_phone_state),
                            new IconicsDrawable(this, GoogleMaterial.Icon.gmd_smartphone)
                                    .sizeDp(24).color(Color.BLACK))
                    );
                }
                if (!rxPermissions.isGranted(WRITE_EXTERNAL_STORAGE)) {
                    list.add(new PermissionBean(
                            ResourceUtil.getString(this,
                                    R.string.permission_write_external_storage),
                            ResourceUtil.getString(this,
                                    R.string.permission_request_describe_write_external_storage),
                            new IconicsDrawable(this, GoogleMaterial.Icon.gmd_folder)
                                    .color(Color.BLACK).sizeDp(24))
                    );
                }
                builder.adapter(new PermissionAdapter(this, list),
                        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                builder.onPositive((materialDialog, dialogAction) -> {
                    rxPermissions.request(READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE)
                            .filter(grant -> !grant)
                            .subscribe(grant -> showPermissionDialog());
                    materialDialog.dismiss();
                });
                builder.positiveText(
                        ResourceUtil.getString(this, R.string.action_grant_permission));
                builder.negativeText(ResourceUtil.getString(this, R.string.action_refuse));
                builder.canceledOnTouchOutside(false);
                builder.build().show();
            }
        }
    }

    private void initTabAndPager() {
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setPageMargin(DisplayUtil.dip2px(mContext, 16));

        mPageAdapter = new IconViewPageAdapter(this, getSupportFragmentManager(), mCustomPicker);

        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setPageMargin(DisplayUtil.dip2px(mContext, -4));

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setOnTabDoubleTapListener(() -> {
            int index = mViewPager.getCurrentItem();
            IconViewPageAdapter adapter = (IconViewPageAdapter) mViewPager.getAdapter();
            IconFragment fragment = (IconFragment) adapter.getItem(index);
            if (fragment.getRecyclerView() != null) {
                fragment.getRecyclerView().smoothScrollToPosition(0);
            }
        });

    }

    private void initDrawer() {

        int textColorRes = R.color.palette_grey_800;
        int subTextColorRes = R.color.palette_grey_600;
        int iconAlpha = 128;
        mDrawer = new DrawerBuilder()
                .withSliderBackgroundColor(Color.WHITE)
                .withCloseOnClick(true)
                .withToolbar(mSearchToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withOnDrawerNavigationListener(view -> {
                    openDrawer();
                    return false;
                })
                .withActivity(mActivity)
                .build();

        mDrawer.addItems(
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("apply")
                        .withIcon(GoogleMaterial.Icon.gmd_palette)
                        .withTextColorRes(textColorRes)
                        .withName(R.string.nav_item_apply),
                new ExpandableDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withIcon(GoogleMaterial.Icon.gmd_mail)
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
                        .withIcon(GoogleMaterial.Icon.gmd_settings)
                        .withTextColorRes(textColorRes)
                        .withName(R.string.nav_item_settings),
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("help")
                        .withIcon(GoogleMaterial.Icon.gmd_help)
                        .withTextColorRes(textColorRes)
                        .withName(R.string.nav_item_help)
        );

        if (PackageUtil.isAlipayInstalled(this)) {
            mDrawer.addItem(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("donate")
                    .withIcon(GoogleMaterial.Icon.gmd_local_atm)
                    .withTextColorRes(textColorRes)
                    .withName(R.string.nav_item_donate));
        }

        if (BuildConfig.DEBUG) {
            mDrawer.addItemAtPosition(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("custom")
                    .withIcon(GoogleMaterial.Icon.gmd_create)
                    .withTextColorRes(textColorRes)
                    .withName(R.string.nav_item_custom_workshop), 1);
            mDrawer.addItems(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("test")
                    .withTextColorRes(textColorRes)
                    .withName("Test"));
        }

        mDrawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            switch ((String) drawerItem.getTag()) {
                case "apply":
                    mNavigator.toAppleActivity();
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
                case "test":
                    mNavigator.toTestActivity();
                    break;
                case "custom":
                    mNavigator.toCustomWorkshopActivity();
                    break;
            }
            return false;
        });

        final RecyclerView drawerRecyclerView = mDrawer.getRecyclerView();
        drawerRecyclerView.setVerticalScrollBarEnabled(false);
        drawerRecyclerView.setHorizontalScrollBarEnabled(false);

        ViewTreeObserver viewTreeObserver = drawerRecyclerView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    drawerRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    BitmapFactory.Options dimensions = new BitmapFactory.Options();
                    dimensions.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(getResources(), R.drawable.drawer_head,
                            dimensions);
                    int height = dimensions.outHeight;
                    int width = dimensions.outWidth;

                    height = (int) Math.ceil(1.0 * height * drawerRecyclerView.getWidth() / width);

                    int titleBarHeight = UIUtils.getStatusBarHeight(MainActivity.this);

                    View view = View.inflate(MainActivity.this, R.layout.layout_drawer_head, null);
                    ImageView image = (ImageView) view.findViewById(R.id.imageView_drawer_head);
                    image.setImageResource(R.drawable.drawer_head);
                    LayoutParams imageLayoutParams = image.getLayoutParams();
                    if (imageLayoutParams == null) {
                        imageLayoutParams = new LayoutParams(MATCH_PARENT, height);
                    } else {
                        imageLayoutParams.height = height;
                    }
                    image.setLayoutParams(imageLayoutParams);

                    View topSpace = view.findViewById(R.id.view_drawer_head_space_top);
                    LayoutParams topSpaceLayoutParams = topSpace.getLayoutParams();
                    if (topSpaceLayoutParams == null) {
                        topSpaceLayoutParams = new LayoutParams(MATCH_PARENT, titleBarHeight);
                    } else {
                        topSpaceLayoutParams.height = titleBarHeight;
                    }
                    topSpace.setLayoutParams(topSpaceLayoutParams);

                    View bottomSpace = view.findViewById(R.id.view_drawer_head_space_bottom);
                    LayoutParams bottomSpaceLayoutParams = bottomSpace.getLayoutParams();
                    int bottomPadding = Math.max(0,
                            DisplayUtil.dip2px(MainActivity.this, 178) - titleBarHeight - height);
                    if (bottomSpaceLayoutParams == null) {
                        bottomSpaceLayoutParams = new LayoutParams(MATCH_PARENT, bottomPadding);
                    } else {
                        bottomSpaceLayoutParams.height = bottomPadding;
                    }
                    bottomSpace.setLayoutParams(bottomSpaceLayoutParams);

                    mDrawer.setHeader(view, false, false);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().postDelayed(() -> {
            mSearchTransitioner.onActivityResumed();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                getWindow().getDecorView().setSystemUiVisibility(0);
//            }
        }, 100);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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
            Timber.e(e);
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

    private static boolean isCustomPicker(Intent intent) {
        return intent.hasCategory("com.novalauncher.category.CUSTOM_ICON_PICKER");
    }
}

