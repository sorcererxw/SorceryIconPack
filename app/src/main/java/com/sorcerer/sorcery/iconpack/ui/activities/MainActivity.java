package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.ViewPageAdapter;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.ToolbarOnGestureListener;
import com.sorcerer.sorcery.iconpack.util.UpdateHelper;

import org.w3c.dom.Text;

import cn.bmob.v3.Bmob;
import im.fir.sdk.FIR;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static Intent mLaunchIntent;
    private ViewPageAdapter mPageAdapter;
    private MaterialSearchView mSearchBox;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private boolean mCustomPicker = false;

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
            new MaterialSearchView.SearchViewListener() {
                @Override
                public void onSearchViewShown() {
                }

                @Override
                public void onSearchViewClosed() {
                    ((IconFragment) mPageAdapter.getItem(mViewPager.getCurrentItem()))
                            .showWithString
                                    ("");
                }
            };

    private MaterialSearchView.OnQueryTextListener mSearchQueryTextListener =
            new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ((IconFragment) mPageAdapter.getItem(mViewPager.getCurrentItem()))
                            .showWithString
                                    (newText);
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(this, SplashActivity.class);
//        startActivity(intent);
        setContentView(R.layout.activity_main);

        mLaunchIntent = getIntent();
        String action = getIntent().getAction();
        if (action.equals("com.novalauncher.THEME")) {
            mCustomPicker = true;
        }

//        if (Build.VERSION.SDK_INT >= 21) {
//            ActivityManager.TaskDescription
//                    taskDescription =
//                    new ActivityManager.TaskDescription(getString(R.string.app_name), null,
//                            getResources().getColor(R.color.colorPrimary));
//            setTaskDescription(taskDescription);
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarDoubleTap(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initTabAndPager();

        initSearchBox();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_main);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_main);
        mNavigationView.setNavigationItemSelectedListener(this);

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
        mDrawerLayout.setDrawerListener(toggle);
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
            showWelcomeDialog();
        } else {
            if (sharedPreferences.getInt("ver", 0) < BuildConfig.VERSION_CODE) {
//                showWelcomeDialog();
                sharedPreferences.edit().putInt("ver", BuildConfig.VERSION_CODE).apply();
            }
        }
        if (launchTimes % 5 == 0) {
//            UpdateHelper updateHelper =
//                    new UpdateHelper(this);
//            updateHelper.update();
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
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout_icon);

        mViewPager = (ViewPager) findViewById(R.id.viewPager_icon);

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
        adapter.addFragment(generateFragment(IconFragment.FLAG_XIAOMI), name[14]);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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
        if (mSearchBox.isSearchOpen()) {
            closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    private void initSearchBox() {
        mSearchBox = (MaterialSearchView) findViewById(R.id.searchBox_main_icon);
        mSearchBox.setOnSearchViewListener(mSearchViewListener);
        mSearchBox.setOnQueryTextListener(mSearchQueryTextListener);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchBox.setMenuItem(menu.findItem(R.id.action_search_icon));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void closeSearch() {
        mSearchBox.clearFocus();
        mSearchBox.closeSearch();
    }
}
