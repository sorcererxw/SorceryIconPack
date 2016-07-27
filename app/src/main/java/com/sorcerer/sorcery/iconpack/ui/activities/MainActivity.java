package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.quinny898.library.persistentsearch.SearchResult;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SorceryIcons;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.ViewPageAdapter;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.ui.views.SearchBox;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;
import com.sorcerer.sorcery.iconpack.util.ToolbarOnGestureListener;
import com.sorcerer.sorcery.iconpack.util.UpdateHelper;

import java.util.Arrays;

import butterknife.BindView;
import im.fir.sdk.FIR;

/**
 * Created by Sorcerer on 2016/6/1 0001.
 * <p/>
 * MainActivity
 * The first activity with drawer and icon viewpager.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mMainToolbar;

    @BindView(R.id.appBarLayout_main)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.viewPager_icon)
    ViewPager mViewPager;

    @BindView(R.id.searchBox_main_icon)
    SearchBox mSearchBox;

    @BindView(R.id.tabLayout_icon)
    TabLayout mTabLayout;

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

                private int times = 0;

                @Override
                public void onPageScrolled(int position, float positionOffset,
                        int positionOffsetPixels) {


                    if (position != mViewPager.getCurrentItem()) {
                        closeSearch();
                    }

                    if (position == 0 && positionOffset == 0 && positionOffsetPixels == 0) {
                        times++;
                        if (times >= 3) {
                            openDrawer();
                        }
                    }
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        times = 0;
                    }
                }
            };

    private SearchBox.SearchListener mSearchListener = new SearchBox.SearchListener() {
        @Override
        public void onSearchOpened() {
            mAppBarLayout.setExpanded(true);
        }

        @Override
        public void onSearchCleared() {

        }

        @Override
        public void onSearchClosed() {
            mSearchBox.clearResults();
        }

        @Override
        public void onSearchTermChanged(String term) {
            ((IconFragment) mPageAdapter.getItem(mViewPager.getCurrentItem()))
                    .showWithString(term.toLowerCase());
        }

        @Override
        public void onSearch(String result) {

        }

        @Override
        public void onResultClick(SearchResult result) {

        }
    };

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        Log.d(TAG, "onCreate");
        mLaunchIntent = getIntent();
        String action = getIntent().getAction();

        mCustomPicker = action.equals("com.novalauncher.THEME");

        setSupportActionBar(mMainToolbar);
        setToolbarDoubleTap(mMainToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initTabAndPager();

        initSearchBox();

        initDrawer();

        if (!mCustomPicker) {

            FIR.init(getApplicationContext());

            UpdateHelper updateHelper =
                    new UpdateHelper(this);
            updateHelper.update();
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.select_an_icon));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("sorcery icon pack",
                MODE_PRIVATE);

        int launchTimes = sharedPreferences.getInt("launch times", 0);
        if (launchTimes == 0) {
            openDrawer();
        } else {
            if (sharedPreferences.getInt("ver", 0) < BuildConfig.VERSION_CODE) {
                sharedPreferences.edit().putInt("ver", BuildConfig.VERSION_CODE).apply();
            }
        }
        if (!sharedPreferences.getBoolean("know help", false)) {
            sharedPreferences.edit().putBoolean("know help", true).apply();
        }

        sharedPreferences.edit().putInt("launch times", launchTimes + 1).apply();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (Build.VERSION.SDK_INT >= 23) {
            PermissionsHelper.requestWriteExternalStorage(this);
            PermissionsHelper.requestReadPhoneState(this);
        }
    }

    private void initTabAndPager() {
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setPageMargin(DisplayUtil.dip2px(mContext, 16));

        mPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        generateFragments(mPageAdapter);
        mViewPager.setAdapter(mPageAdapter);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initSearchBox() {
        mSearchBox.setLogoText("Sorcery Icons");
        mSearchBox.setHint(ResourceUtil.getString(mContext, R.string.search_hint));
        mSearchBox.setSearchListener(mSearchListener);
        mSearchBox.setSearchWithoutSuggestions(true);
        mSearchBox.setMenuListener(new SearchBox.MenuListener() {
            @Override
            public void onMenuClick() {
                openDrawer();
            }
        });
        Typeface typeface = Typeface.createFromAsset(getAssets(), "RockwellStd.otf");
        mSearchBox.setLogoTypeface(typeface);
        mSearchBox.setAnimateDrawerLogo(true);
    }

    private void initDrawer() {
        mDrawer = new DrawerBuilder()
                .withCloseOnClick(true)
                .withAccountHeader(new AccountHeaderBuilder()
                        .withActivity(this)
                        .withHeightDp(178)
                        .withHeaderBackground(R.drawable.sandy_shore)
                        .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                        .withProfileImagesClickable(false)
                        .withResetDrawerOnProfileListClick(false)
                        .withSelectionListEnabled(false)
                        .withSelectionListEnabledForSingleProfile(false)
                        .build())
                .withActivity(mActivity)
                .build();

        mDrawer.addItems(
                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withTag("apply")
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                                .ic_input_black_24dp, 128))
                        .withName(R.string.nav_item_apply),
                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withTag("feedback")
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                                .ic_mail_black_24dp, 128))
                        .withName(R.string.nav_item_feedback),
                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withTag("lab")
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                                .ic_settings_black_24dp, 128))
                        .withName(R.string.nav_item_lab),
                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withTag("help")
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                                .ic_help_black_24dp, 128))
                        .withName(R.string.nav_item_help),
                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withTag("donate")
                        .withIcon(ResourceUtil.getDrawableWithAlpha(mContext, R.drawable
                                .ic_attach_money_black_24dp, 128))
                        .withName(R.string.nav_item_donate),
                new DividerDrawerItem(),
                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withTag("update")
                        .withName(R.string.nav_item_update),
                new PrimaryDrawerItem()
                        .withSelectable(false)
                        .withTag("about")
                        .withName(R.string.nav_item_about)
        );
        if (SorceryIcons.DEBUG) {
            mDrawer.addItem(new PrimaryDrawerItem().withSelectable(false).withTag("DEBUG")
                    .withName("DEBUG"));
        }

        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch ((String) drawerItem.getTag()) {
                    case "apply":
                        activityShift(ApplyActivity.class);
                        break;
                    case "feedback":
                        activityShift(FeedbackActivity.class);
                        break;
                    case "lab":
                        activityShift(LabActivity.class);
                        break;
                    case "help":
                        activityShift(HelpActivity.class);
                        break;
                    case "donate":
                        activityShift(DonateActivity.class);
                        break;
                    case "update":
                        UpdateHelper updateHelper =
                                new UpdateHelper(mContext,
                                        mCoordinatorLayout);
                        updateHelper.update();
                        break;
                    case "about":
                        Intent intent = new Intent(mContext, AboutDialogActivity.class);
                        mContext.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.fade_in, 0);
                        break;
                    case "DEBUG":
                        activityShift(TestActivity.class);
                        break;
                }
                return false;
            }
        });

//        ViewGroup.LayoutParams params = mDrawer.getDrawerLayout().getLayoutParams();
//        params.width

//        mDrawer.getDrawerLayout().setLayoutParams(mDrawer.getDrawerLayout());
//        ViewGroup.LayoutParams params = mDrawer.getSlider().getLayoutParams();
//        params.width = 100;
//        mDrawer.getSlider().setLayoutParams(params);
    }

    private void setToolbarDoubleTap(Toolbar toolbar) {
        final GestureDetector detector = new GestureDetector(this,
                new ToolbarOnGestureListener(new ToolbarOnGestureListener.DoubleTapListener() {
                    @Override
                    public void onDoubleTap() {
                        int index = mViewPager.getCurrentItem();
                        ViewPageAdapter adapter = (ViewPageAdapter) mViewPager.getAdapter();
                        IconFragment fragment = (IconFragment) adapter.getItem(index);
                        fragment.getRecyclerView().smoothScrollToPosition(0);
                    }
                }));
        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void generateFragments(ViewPageAdapter adapter) {

        String[] name = getResources().getStringArray(R.array.tab_name);
        IconFragment.Flag[] flag = IconFragment.Flag.values();

        Log.d(TAG, Arrays.toString(flag));

        for (int i = 0; i < flag.length; i++) {
            adapter.addFragment(IconFragment.newInstance(flag[i], mCustomPicker), name[i]);
        }
    }

    private void activityShift(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == PermissionsHelper.WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new UpdateHelper(this).update();
            } else {
                Toast.makeText(this, getString(R.string.please_give_permission), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed();
        }
    }

    private void closeSearch() {
        mSearchBox.closeSearch();
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
}
