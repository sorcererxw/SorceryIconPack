package com.sorcerer.sorcery.iconpack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialize.util.UIUtils;
import com.sorcerer.sorcery.iconpack.network.avos.AvosStatisticManager;
import com.sorcerer.sorcery.iconpack.showcase.overview.IconTabFragment;
import com.sorcerer.sorcery.iconpack.ui.activities.WelcomeActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.anim.SearchTransitioner;
import com.sorcerer.sorcery.iconpack.ui.anim.ViewFader;
import com.sorcerer.sorcery.iconpack.ui.views.ExposedSearchToolbar;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Sorcerer on 2016/6/1 0001.
 * <p>
 * <p>
 * MainActivity
 * The first activity with drawer and icon viewpager.
 */
public class MainActivity extends BaseActivity {
    public static final int REQUEST_ICON_DIALOG = 100;
    private static final boolean ENABLE_GUIDE = false;
    @BindView(R.id.exposedSearchToolbar)
    ExposedSearchToolbar mSearchToolbar;
    @BindView(R.id.coordinatorLayout_main)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.appBarLayout_main)
    AppBarLayout mAppBarLayout;
    private Drawer mDrawer;
    private boolean isCustomPicker = false;
    private Navigator mNavigator;
    private SearchTransitioner mSearchTransitioner;
    private IconTabFragment mIconTabFragment;

    @Override
    protected void hookBeforeSetContentView() {
        super.hookBeforeSetContentView();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG) {
            new AvosStatisticManager(this).post();
        }

        getWindow().setBackgroundDrawable(null);

        isCustomPicker = isCustomPicker();

        setSupportActionBar(mSearchToolbar);

        mNavigator = new Navigator(this);

        initDrawer();

        mSearchToolbar.setOnClickListener(view -> {
            mAppBarLayout.setExpanded(true, true);
            mAppBarLayout.post(() -> mSearchTransitioner.transitionToSearch());
        });
        mSearchToolbar.setOnLongClickListener(view -> {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(200);
            }
            mAppBarLayout.setExpanded(true, true);
            mAppBarLayout.post(() -> mSearchTransitioner.transitionToSearch());
            return true;
        });
        mSearchToolbar.setTitle("Sorcery Icons");

        if (isCustomPicker) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setTitle(getString(R.string.select_an_icon));
            }
        }
    }

    private void initDrawer() {

        mDrawer = new DrawerBuilder()
                .withSliderBackgroundColor(ResourceUtil.getAttrColor(this, R.attr.colorCard))
                .withCloseOnClick(false)
                .withToolbar(mSearchToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withOnDrawerNavigationListener(clickedView -> {
                    openDrawer();
                    return true;
                })
                .withActivity(mActivity)
                .build();

        int textColor = ResourceUtil.getAttrColor(this, android.R.attr.textColorPrimary);
        int subTextColor = ResourceUtil.getAttrColor(this, android.R.attr.textColorSecondary);
        int iconColor = ResourceUtil.getAttrColor(this, android.R.attr.textColorSecondary);
        int iconSize = 48;

        mDrawer.addItems(
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("apply")
                        .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_palette)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.nav_item_apply),
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("request")
                        .withIcon(new IconicsDrawable(this,
                                GoogleMaterial.Icon.gmd_playlist_add_check)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.request),
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("settings")
                        .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_settings)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.nav_item_settings),
                new PrimaryDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withTag("help")
                        .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_help)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.nav_item_help)
                        .withDescription(R.string.nav_item_help_description)
                        .withDescriptionTextColor(subTextColor)
        );

        if (PackageUtil.isAbleToDonate(this)) {
            mDrawer.addItem(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("donate")
                    .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_local_atm)
                            .color(iconColor).sizeDp(iconSize))
                    .withTextColor(textColor)
                    .withName(R.string.nav_item_donate));
        }
        mDrawer.addItem(new DividerDrawerItem());
        mDrawer.addItem(new PrimaryDrawerItem()
                .withSetSelected(false)
                .withSelectable(false)
                .withTag("suggest")
                .withTextColor(subTextColor)
                .withName(R.string.nav_item_feedback));

        if (BuildConfig.DEBUG) {
            mDrawer.addItemAtPosition(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("custom")
                    .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_create)
                            .color(iconColor).sizeDp(iconSize))
                    .withTextColor(textColor)
                    .withName(R.string.nav_item_custom_workshop), 1);
            mDrawer.addItem(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("test")
                    .withTextColor(subTextColor)
                    .withName("Test"));
        }

        mDrawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            if (drawerItem.getTag().equals("apply")) {
                mNavigator.toAppleActivity();
            }
            if (drawerItem.getTag().equals("settings")) {
                mNavigator.toSettingsActivity();
            }
            if (drawerItem.getTag().equals("help")) {
                mNavigator.toHelpMarkdownActivity();
            }
            if (drawerItem.getTag().equals("donate")) {
                mNavigator.toDonateActivity();
            }
            if (drawerItem.getTag().equals("request")) {
                mNavigator.toIconRequest();
            }
            if (drawerItem.getTag().equals("suggest")) {
                showFeedbackOptionPanel();
            }
            if (drawerItem.getTag().equals("test")) {
                mNavigator.toTestActivity();
            }
            new Handler().post(this::closeDrawer);
            return false;
        });

        RecyclerView drawerRecyclerView = mDrawer.getRecyclerView();
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
                    BitmapFactory.decodeResource(getResources(), R.drawable.drawer_head_simple,
                            dimensions);
                    int height = dimensions.outHeight;
                    int width = dimensions.outWidth;

                    height = (int) Math.ceil(1.0 * height * drawerRecyclerView.getWidth() / width);

                    View view = View.inflate(MainActivity.this, R.layout.layout_drawer_head, null);
                    ImageView image = view.findViewById(R.id.imageView_drawer_head);

                    Drawable drawable = ResourceUtil
                            .getDrawable(MainActivity.this, R.drawable.drawer_head_simple);
                    image.setImageDrawable(drawable);
                    LayoutParams imageLayoutParams = image.getLayoutParams();
                    if (imageLayoutParams == null) {
                        imageLayoutParams = new LayoutParams(MATCH_PARENT, height);
                    } else {
                        imageLayoutParams.height = height;
                    }
                    image.setLayoutParams(imageLayoutParams);


                    if (mPrefs.nightMode()) {
                        image.setImageDrawable(new ColorDrawable(
                                ResourceUtil.getAttrColor(mContext, R.attr.colorCard)));
                    }

                    View bottomSpace =
                            view.findViewById(R.id.view_drawer_head_space_bottom);
                    LayoutParams bottomSpaceLayoutParams = bottomSpace.getLayoutParams();
                    int bottomPadding = (int) Math.max(0,
                            UIUtils.convertDpToPixel(178f, MainActivity.this) - height
                    );
                    if (bottomSpaceLayoutParams == null) {
                        bottomSpaceLayoutParams = new LayoutParams(MATCH_PARENT, bottomPadding);
                    } else {
                        bottomSpaceLayoutParams.height = bottomPadding;
                    }
                    bottomSpace.setLayoutParams(bottomSpaceLayoutParams);

                    mDrawer.setHeader(view, false, false);

                    mPrefs.firstTimeLaunch().asObservable()
                            .filter(firstTime -> firstTime)
                            .observeOn(Schedulers.newThread())
                            .delay(2000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aBoolean -> {
                                mDrawer.openDrawer();
                                mPrefs.firstTimeLaunch().set(false);
                            }, Timber::e);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIconTabFragment == null) {
            mIconTabFragment = IconTabFragment.newInstance(isCustomPicker);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.linearLayout_content_main, mIconTabFragment)
                    .commitNow();
            mSearchTransitioner = new SearchTransitioner(this,
                    new Navigator(this),
                    mIconTabFragment.getTabLayout(),
                    mIconTabFragment.getViewPager(),
                    mSearchToolbar,
                    new ViewFader());
        }

        getWindow().getDecorView().postDelayed(() -> {
            if (mSearchTransitioner != null) {
                mSearchTransitioner.onActivityResumed();
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            if (Math.abs(mAppBarLayout.getY()) < 0.01f) {
                super.onBackPressed();
            } else {
                mAppBarLayout.setExpanded(true, true);
            }
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

    private void showFeedbackOptionPanel() {
        if (!Navigator.toMail(this,
                "feedback@sorcererxw.com",
                "Sorcery Icons feedback",
                "")) {
            Snackbar.make(mDrawer.getDrawerLayout(), R.string.feedback_need_mailbox,
                    Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_install, view ->
                            Navigator.toAppMarketSearchResult(MainActivity.this,
                                    ResourceUtil.getString(MainActivity.this,
                                            R.string.market_search_mail))
                    ).show();
        }
//        var builder = BottomSheetDialogBuilder(this)
//        builder.build().show()
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
            String bmUri = "android.resource://" + mContext.getPackageName() + "/" + res;
            intent.setData(Uri.parse(bmUri));
            setResult(Activity.RESULT_OK, intent);
        } else {
            setResult(Activity.RESULT_CANCELED, intent);
        }
        finish();
    }

    public boolean isCustomPicker() {
        return getIntent().hasCategory("com.novalauncher.category.CUSTOM_ICON_PICKER");
    }
}