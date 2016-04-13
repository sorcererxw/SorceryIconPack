package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.DrawerMenuAdapter;
import com.sorcerer.sorcery.iconpack.adapters.ViewPageAdapter;
import com.sorcerer.sorcery.iconpack.models.SorceryMenuItem;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.ui.views.MyMaterialSearchView;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.ToolbarOnGestureListener;
import com.sorcerer.sorcery.iconpack.util.UpdateHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import im.fir.sdk.FIR;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_ICON_DIALOG = 100;

    public static Intent mLaunchIntent;
    private ViewPageAdapter mPageAdapter;
    private MaterialSearchView mSearchBox;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private boolean mCustomPicker = false;
    private Context mContext = this;
    private Activity mActivity = this;
    private RecyclerView mMenuView;
    private boolean mCloseEnable = true;

    private ViewPager.OnPageChangeListener mPageChangeListener =

            new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                    if (position != mViewPager.getCurrentItem()
                            && mSearchBox.isSearchOpen()) {
                        closeSearch();
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    if (position != mViewPager.getCurrentItem() && mSearchBox.isSearchOpen()) {
                        closeSearch();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    private MaterialSearchView.SearchViewListener mSearchViewListener =
            new MyMaterialSearchView.SearchViewListener() {

                @Override
                public void onSearchViewShown() {
                    mCloseEnable = false;
                    mViewPager.setCurrentItem(1);
                    mSearchBox.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCloseEnable = true;
                        }
                    }, 300);
                    mSearchBox.showSuggestions();
                }

                @Override
                public void onSearchViewClosed() {
                    for (int i = 0; i < mPageAdapter.getCount(); i++) {
                        ((IconFragment) mPageAdapter.getItem(i)).showWithString("");
                    }
                }
            };

    private MaterialSearchView.OnQueryTextListener mSearchQueryTextListener =
            new MyMaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ((IconFragment) mPageAdapter.getItem(mViewPager.getCurrentItem()))
                            .showWithString(newText.toLowerCase());
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLaunchIntent = getIntent();
        String action = getIntent().getAction();

        mCustomPicker = action.equals("com.novalauncher.THEME");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarDoubleTap(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initTabAndPager();

        initSearchBox();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_main);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_main);
        assert mNavigationView != null;
        mNavigationView.setNavigationItemSelectedListener(this);
        initDrawerView();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.nav_open, R.string.nav_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
                if (mSearchBox.isSearchOpen()) {
                    closeSearch();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (!mCustomPicker) {
            FIR.init(getApplicationContext());

            Bmob.initialize(this, getString(R.string.bmob_app_id));
            UpdateHelper updateHelper =
                    new UpdateHelper(this);
            updateHelper.update();
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.select_an_icon));
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("sorcery icon pack",
                MODE_PRIVATE);
        int launchTimes = sharedPreferences.getInt("launch times", 0);
        Log.d("sip", "launch time " + launchTimes);
        if (launchTimes == 0) {

        } else {
            if (sharedPreferences.getInt("ver", 0) < BuildConfig.VERSION_CODE) {
                sharedPreferences.edit().putInt("ver", BuildConfig.VERSION_CODE).apply();
                ImageLoader.getInstance().clearDiskCache();
            }
        }
        if (!sharedPreferences.getBoolean("know help", false)) {
//            showWelcomeDialog();
            sharedPreferences.edit().putBoolean("know help", true).apply();
        }

//        if (!sharedPreferences.getBoolean("showed permission dialog", false)) {
//            showPermissionDialog();
//        }

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
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout_icon);

        mViewPager = (ViewPager) findViewById(R.id.viewPager_icon);

        assert mViewPager != null;
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.addOnPageChangeListener(mPageChangeListener);

        mPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        generateFragments(mPageAdapter);
        mViewPager.setAdapter(mPageAdapter);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void showWelcomeDialog() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.welcome_title))
                .content(getString(R.string.welcome_body))
                .positiveText(getString(R.string.welcome_close))
                .build().show();
    }

    private void showPermissionDialog() {
        boolean show = PermissionsHelper.hasPermission(mActivity, PermissionsHelper
                .READ_PHONE_STATE_MANIFEST) && PermissionsHelper.hasPermission(mActivity,
                PermissionsHelper.WRITE_EXTERNAL_STORAGE_MANIFEST);

        if (Build.VERSION.SDK_INT >= 23 && !show) {
            new MaterialDialog.Builder(this)
                    .canceledOnTouchOutside(false)
                    .title(R.string.permission_request_title)
                    .content(R.string.permission_request_content)
                    .positiveText(R.string.action_give_permission)
                    .negativeText(R.string.no)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog,
                                            @NonNull DialogAction which) {
                            if (which == DialogAction.POSITIVE) {if (!PermissionsHelper.hasPermission(mActivity, PermissionsHelper
                                        .WRITE_EXTERNAL_STORAGE_MANIFEST)) {
                                    PermissionsHelper.requestWriteExternalStorage(mActivity);
                                } else {
                                    PermissionsHelper.requestReadPhoneState(mActivity);
                                }
                            } else {
                                getSharedPreferences("sorcery icon pack",
                                        MODE_PRIVATE).edit().putBoolean("showed permission " +
                                        "dialog", true);
                            }
                        }
                    }).show();
        }
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

    private IconFragment getIconFragment(int flag) {
        Bundle args = new Bundle();
        args.putInt("flag", flag);
        IconFragment fragment = new IconFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void generateFragments(ViewPageAdapter adapter) {

        String[] name = getResources().getStringArray(R.array.tab_name);

        adapter.addFragment(generateFragment(IconFragment.FLAG_NEW), name[0]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_ALL), name[1]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_ALI), name[2]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_BAIDU), name[3]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_CYANOGENMOD), name[4]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_GOOGLE), name[5]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_HTC), name[6]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_LENOVO), name[7]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_LG), name[8]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_MOTO), name[9]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_MICROSOFT), name[10]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_SAMSUNG), name[11]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_SONY), name[12]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_TENCENT), name[13]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_MIUI), name[14]);
        adapter.addFragment(generateFragment(IconFragment.FLAG_FLYME), name[15]);
    }

    private IconFragment generateFragment(int flag) {
        IconFragment iconFragment = getIconFragment(flag);
        iconFragment.setSearchListener(new IconFragment.SearchListener() {
            @Override
            public void onSearch() {
                mSearchBox.showSearch();
            }
        });
        iconFragment.setCustomPicker(this, mCustomPicker);
        return iconFragment;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_item_icon) {

        } else if (id == R.id.nav_item_apply) {
            activityShift(ApplyActivity.class);
        } else if (id == R.id.nav_item_feedback) {
            activityShift(FeedbackActivity.class);
        } else if (id == R.id.nav_item_settings) {
            activityShift(SettingsActivity.class);
        } else if (id == R.id.nav_item_lab) {
            activityShift(LabActivity.class);
        } else if (id == R.id
                .nav_item_welcome) {
            showWelcomeDialog();
        } else if (id == R.id.nav_item_update) {
            UpdateHelper updateHelper =
                    new UpdateHelper(this, findViewById(R.id.coordinatorLayout_main));
            updateHelper.update();
        } else if (id == R.id.nav_item_donate) {
            activityShift(DonateActivity.class);
        } else if (id == R.id.nav_item_test) {
            activityShift(TestActivity.class);
        } else if (id == R.id.nav_item_about) {
            activityShift(AboutActivity.class);
        } else if (id == R.id.nav_item_help) {
            activityShift(HelpActivity.class);
        }
        mDrawerLayout.closeDrawers();
        return true;
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
//        if (!PermissionsHelper.hasPermission(this, PermissionsHelper
//                .WRITE_EXTERNAL_STORAGE_MANIFEST)) {
//            PermissionsHelper.requestWriteExternalStorage(this);
//        }
//        if (!PermissionsHelper.hasPermission(this, PermissionsHelper
//                .READ_PHONE_STATE_MANIFEST)) {
//            PermissionsHelper.requestReadPhoneState(this);
//        }

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
        if (mSearchBox.isSearchOpen()) {
            closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void initSearchBox() {
        mSearchBox = (MaterialSearchView) findViewById(R.id.searchBox_main_icon);
        assert mSearchBox != null;
        mSearchBox.setOnSearchViewListener(mSearchViewListener);
        mSearchBox.setOnQueryTextListener(mSearchQueryTextListener);
        String[] suggestions = getResources().getStringArray(R.array.search_suggestion);
        mSearchBox.setSuggestions(suggestions);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchBox.setMenuItem(menu.findItem(R.id.action_search_icon));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void closeSearch() {
        if (mSearchBox.isSearchOpen() && mCloseEnable) {
            mSearchBox.clearFocus();
            mSearchBox.closeSearch();
        }
    }

    private void initDrawerView() {
        View head = mNavigationView.getHeaderView(0);

        mMenuView = (RecyclerView) head.findViewById(R.id.recyclerView_drawer_menu);

        List<SorceryMenuItem.OnSelectListener> listeners = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            final int finalI = i;
            listeners.add(new SorceryMenuItem.OnSelectListener() {
                @Override
                public void onSelect() {
                    if (finalI == 0) {
                        activityShift(ApplyActivity.class);
                    } else if (finalI == 1) {
                        activityShift(FeedbackActivity.class);
                    } else if (finalI == 2) {
                        activityShift(LabActivity.class);
                    } else if (finalI == 3) {
                        UpdateHelper updateHelper =
                                new UpdateHelper(mContext,
                                        findViewById(R.id.coordinatorLayout_main));
                        updateHelper.update();
                    } else if (finalI == 4) {
                        activityShift(HelpActivity.class);
                    } else if (finalI == 5) {
                        activityShift(DonateActivity.class);
                    } else if (finalI == 6) {
                        activityShift(AboutActivity.class);
                    }
                    mDrawerLayout.closeDrawers();
                }
            });
        }

        List<SorceryMenuItem> list = new ArrayList<>();
        list.add(new SorceryMenuItem(listeners.get(0), R.drawable.ic_input_black_24dp,
                getString(R.string.nav_item_apply)));
        list.add(new SorceryMenuItem(listeners.get(1), R.drawable.ic_mail_black_24dp,
                getString(R.string.nav_item_feedback)));
        list.add(new SorceryMenuItem(listeners.get(2), R.drawable.ic_settings_black_24dp,
                getString(R.string.nav_item_lab)));
        list.add(new SorceryMenuItem(listeners.get(3),
                R.drawable.ic_system_update_black_24dp,
                getString(R.string.nav_item_update)));
        list.add(new SorceryMenuItem(listeners.get(4), R.drawable.ic_help_black_24dp,
                getString(R.string.nav_item_help)));
        list.add(new SorceryMenuItem(listeners.get(5), R.drawable.ic_attach_money_black_24dp,
                getString(R.string.nav_item_donate)));
        list.add(new SorceryMenuItem(listeners.get(6), R.drawable.ic_face_black_24dp,
                getString(R.string.nav_item_about)));

        DrawerMenuAdapter adapter = new DrawerMenuAdapter(this, list);
        mMenuView.setAdapter(adapter);
        mMenuView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        mMenuView.setHasFixedSize(true);
    }

}
