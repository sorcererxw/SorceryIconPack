package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.ViewPageAdapter;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.ToolbarOnGestureListener;
import com.sorcerer.sorcery.iconpack.util.UpdateHelper;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
//    private HollyViewPager mHollyViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setToolbarDoubleTap(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout_icon);

//        mHollyViewPager = (HollyViewPager) findViewById(R.id.hollyViewPager_icon);
        mViewPager = (ViewPager) findViewById(R.id.viewPager_icon);

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        generateFragments(adapter);
        mViewPager.setAdapter(adapter);
//        mViewPager.setPageMargin(100);
//        mHollyViewPager.setAdapter(adapter);
//        mHollyViewPager.setConfigurator(new HollyViewPagerConfigurator() {
//            @Override
//            public float getHeightPercentForPage(int page) {
//                return ((page+4)%10)/10f;
//            }
//        });
//        mHollyViewPager.getViewPager().setPageMargin(16);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);

        UpdateHelper updateHelper =
                new UpdateHelper(this);
        updateHelper.update();
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
//                        int index = mHollyViewPager.getViewPager().getCurrentItem();
//                        ViewPageAdapter adapter = (ViewPageAdapter) mHollyViewPager.getViewPager()
//                                .getAdapter();
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

    /*
    <string-array name="tab_name">
        <item>new</item>
        <item>all</item>
        <item>ALi</item>
        <item>Baidu</item>
        <item>Cyanogenmod</item>
        <item>Google</item>
        <item>HTC</item>
        <item>Lenovo</item>
        <item>LG</item>
        <item>Moto</item>
        <item>Microsoft</item>
        <item>Samsung</item>
        <item>SONY</item>
        <item>Tencent</item>
        <item>Xiaomi</item>
    </string-array>
     */

    private void generateFragments(ViewPageAdapter adapter) {
        String[] name = getResources().getStringArray(R.array.tab_name);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_NEW), name[0]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_ALL), name[1]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_ALI), name[2]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_BAIDU), name[3]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_CYANOGENMOD), name[4]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_GOOGLE), name[5]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_HTC), name[6]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_LENOVO), name[7]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_LG), name[8]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_MOTO), name[9]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_MICROSOFT), name[10]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_SAMSUNG), name[11]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_SONY), name[12]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_TENCENT), name[13]);
        adapter.addFragment(getIconFragment(IconFragment.FLAG_XIAOMI), name[14]);

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
        } else if (id == R.id.nav_item_welcome) {
            showWelcomeDialog();
        } else if (id == R.id.nav_item_update) {
            UpdateHelper updateHelper =
                    new UpdateHelper(this, findViewById(R.id.coordinatorLayout_main));
            updateHelper.update();
        } else if (id == R.id.nav_item_donate) {
            activityShift(DonateActivity.class);
        } else if (id == R.id.nav_item_test) {
            activityShift(TestActivity.class);
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
                Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
