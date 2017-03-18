package com.sorcerer.sorcery.iconpack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialize.util.UIUtils;
import com.sorcerer.sorcery.iconpack.iconShowCase.overview.IconTabFragment;
import com.sorcerer.sorcery.iconpack.ui.Navigator;
import com.sorcerer.sorcery.iconpack.ui.activities.WelcomeActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.anim.SearchTransitioner;
import com.sorcerer.sorcery.iconpack.ui.anim.ViewFader;
import com.sorcerer.sorcery.iconpack.ui.views.ExposedSearchToolbar;
import com.sorcerer.sorcery.iconpack.utils.DisplayUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import butterknife.BindView;
import timber.log.Timber;

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

    @BindView(R.id.coordinatorLayout_main)
    CoordinatorLayout mCoordinatorLayout;

    private Drawer mDrawer;

    public static final int REQUEST_ICON_DIALOG = 100;

    private boolean mCustomPicker = false;

    private Navigator mNavigator;
    private SearchTransitioner mSearchTransitioner;

    private IconTabFragment mIconTabFragment;

    private static final boolean ENABLE_GUIDE = false;

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
    protected void init(Bundle savedInstanceState) {
        if (!PackageUtil.isInstallFromPlay(this) && BuildConfig.ONLY_FOR_PLAY) {
            Toast.makeText(mContext, "This version can only be installed by google play to use",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        getWindow().setBackgroundDrawable(null);

        mCustomPicker = isCustomPicker(getIntent());

        setSupportActionBar(mSearchToolbar);

        mNavigator = new Navigator(this);

        mDrawer = new DrawerBuilder()
                .withSliderBackgroundColor(ResourceUtil.getAttrColor(this, R.attr.colorCard))
                .withCloseOnClick(true)
                .withToolbar(mSearchToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withOnDrawerNavigationListener(view -> {
                    openDrawer();
                    return true;
                })
//                .withDisplayBelowStatusBar(true)
//                .withTranslucentStatusBar(true)
                .withActivity(mActivity)
                .build();

        initDrawer();

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

    private void initDrawer() {

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
                new ExpandableDrawerItem()
                        .withSetSelected(false)
                        .withSelectable(false)
                        .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_mail)
                                .color(iconColor).sizeDp(iconSize))
                        .withTextColor(textColor)
                        .withName(R.string.nav_item_feedback)
                        .withTag("")
                        .withArrowColor(iconColor)
                        .withSubItems(
                                new SecondaryDrawerItem()
                                        .withLevel(3)
                                        .withSetSelected(false)
                                        .withSelectable(false)
                                        .withName(R.string.request)
                                        .withTextColor(subTextColor)
                                        .withTag("request"),
                                new SecondaryDrawerItem()
                                        .withLevel(3)
                                        .withSetSelected(false)
                                        .withSelectable(false)
                                        .withTextColor(subTextColor)
                                        .withName(R.string.suggest)
                                        .withTag("suggest")
                        ),
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
        );

        if (PackageUtil.isAlipayInstalled(this)) {
            mDrawer.addItem(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("donate")
                    .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_local_atm)
                            .color(iconColor).sizeDp(iconSize))
                    .withTextColor(textColor)
                    .withName(R.string.nav_item_donate));
        }

        if (BuildConfig.DEBUG) {
            mDrawer.addItemAtPosition(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("custom")
                    .withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_create)
                            .color(iconColor).sizeDp(iconSize))
                    .withTextColor(textColor)
                    .withName(R.string.nav_item_custom_workshop), 1);
            mDrawer.addItems(new PrimaryDrawerItem()
                    .withSetSelected(false)
                    .withSelectable(false)
                    .withTag("test")
                    .withTextColor(textColor)
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

                    if (mThemeManager.getCurrentTheme().isDark()) {
                        image.setImageDrawable(new ColorDrawable(
                                ResourceUtil.getAttrColor(mContext, R.attr.colorCard)));
                    }

                    View topSpace = view.findViewById(R.id.view_drawer_head_space_top);
                    LayoutParams topSpaceLayoutParams = topSpace.getLayoutParams();
                    if (topSpaceLayoutParams == null) {
                        topSpaceLayoutParams = new LayoutParams(MATCH_PARENT, titleBarHeight);
                    } else {
                        topSpaceLayoutParams.height = titleBarHeight;
                    }
                    topSpace.setLayoutParams(topSpaceLayoutParams);

                    if (mThemeManager.getCurrentTheme().isDark()) {
                        topSpace.setBackground(new ColorDrawable(
                                ResourceUtil.getAttrColor(mContext, R.attr.colorCard)));
                    }

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
    public void resetContentView() {
        super.resetContentView();
        mIconTabFragment = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIconTabFragment == null) {
            mIconTabFragment = IconTabFragment.newInstance(mCustomPicker);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.linearLayout_content_main, mIconTabFragment)
                    .commitNow();
        }
        mSearchTransitioner = new SearchTransitioner(this,
                new Navigator(this),
                mIconTabFragment.getTabLayout(),
                mIconTabFragment.getViewPager(),
                mSearchToolbar,
                new ViewFader());
        getWindow().getDecorView().postDelayed(() -> {
            mSearchTransitioner.onActivityResumed();
        }, 100);
    }

    @Override
    protected ViewGroup rootView() {
        return mCoordinatorLayout;
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