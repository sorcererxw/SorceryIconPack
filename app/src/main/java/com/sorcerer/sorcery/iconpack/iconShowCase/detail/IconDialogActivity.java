package com.sorcerer.sorcery.iconpack.iconShowCase.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.net.spiders.models.AppDisplayInfo;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.base.ToolbarActivity;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;
import com.sorcerer.sorcery.iconpack.utils.LocaleUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.StringUtil;
import com.sorcerer.sorcery.iconpack.utils.ViewUtil;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.sorcerer.sorcery.iconpack.utils.DisplayUtil.dip2px;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/3/22 0022
 */

public class IconDialogActivity extends ToolbarActivity {
    @BindView(R.id.toolbar_icon_dialog)
    Toolbar mToolbar;

    @BindView(R.id.likeLayout)
    LikeLayout mLikeLayout;

    @BindView(R.id.textView_dialog_title)
    TextView mTitleTextView;

    @BindView(R.id.imageView_dialog_icon)
    ImageView mIconImageView;

    @BindView(R.id.relativeLayout_icon_dialog_background)
    View mBackground;

    @BindView(R.id.linearLayout_dialog_icon_show)
    ViewGroup mRoot;

    @BindView(R.id.frameLayout_dialog_icon_component_info_container)
    FrameLayout mComponentContainer;

    @BindView(R.id.linearLayout_dialog_title_container)
    LinearLayout mTitleContainer;

    public static final String EXTRA_RES = "EXTRA_RES";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_LABEL = "EXTRA_LABEL";

    private String mLabel;
    private String mName;
    private int mRes;
    private String mComponent;
    private String mPackageName;

    private ImageView mOriginImage;

    @Override
    protected int provideLayoutId() {
        return R.layout.dialog_icon_show;
    }

    @Override
    protected void hookBeforeSetContentView() {
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);
//        window.setNavigationBarColor(0x33000000);
//
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setEnterTransition(new Fade());
//        getWindow().setExitTransition(new Fade());
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }


    @Override
    protected void init() {

//        new MaterializeBuilder()
//                .withActivity(this)
//                .withStatusBarPadding(true)
//                .withTranslucentStatusBarProgrammatically(true)
//                .withStatusBarColor(ContextCompat.getColor(this, R.color.palette_transparent))
//                .withTransparentNavigationBar(true)
//                .withTransparentStatusBar(true)
//                .build();

        mLabel = getIntent().getStringExtra(EXTRA_LABEL);
        mName = getIntent().getStringExtra(EXTRA_NAME);
        mRes = getIntent().getIntExtra(EXTRA_RES, 0);

        if (mRes == 0) {
            this.finish();
        }

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        mTitleTextView.setText(mLabel);

        mIconImageView.setImageResource(mRes);

        mLikeLayout.bindIcon(mName);

        mBackground.setOnTouchListener((v, event) -> {
            if (!ViewUtil.isPointInsideView(event.getX(),
                    event.getY(),
                    findViewById(R.id.cardView_icon_dialog_card))) {
                onBackPressed();
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Observable.just(mName)
                .map(name -> PackageUtil.getComponentByName(this, name))
                .filter(s -> !TextUtils.isEmpty(s))
                .observeOn(Schedulers.newThread())
                .flatMap(new Function<String, ObservableSource<AppDisplayInfo>>() {
                    @Override
                    public ObservableSource<AppDisplayInfo> apply(String component)
                            throws Exception {
                        mComponent = component;
                        mPackageName = StringUtil.componentInfoToPackageName(mComponent);
                        Timber.d(mPackageName);
                        return AppDisplayInfoGetter
                                .getAppDisplayInfo(mPackageName, IconDialogActivity.this,
                                        LocaleUtil.isChinese(IconDialogActivity.this));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(info -> {
                    if (!TextUtils.isEmpty(info.getAppName())) {
                        mTitleTextView.postDelayed(() -> showRealName(info.getAppName()), 100);
                    }
                    onCreateOptionsMenu(mMenu);
                    return info;
                })
                .observeOn(Schedulers.newThread())
                .map(info -> {
                    if (info.getIcon() == null && info.getIconUrl() != null) {
                        try {
                            info.setIcon(Glide.with(IconDialogActivity.this)
                                    .load(info.getIconUrl()).into(-1, -1)
                                    .get());
                        } catch (InterruptedException | ExecutionException e) {
                            Timber.e(e);
                        }
                    }
                    return info;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(info -> mTitleTextView.postDelayed(() -> {
                    if (mMenu != null && info.getIcon() != null) {
                        MenuItem showOrigin = mMenu.add(Menu.NONE, Menu.NONE, Menu.FIRST,
                                R.string.action_show_origin_icon);
                        showOrigin.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
                        showOrigin.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                        showOrigin.setIcon(
                                new IconicsDrawable(mContext, GoogleMaterial.Icon.gmd_compare)
                                        .sizeDp(24).paddingDp(2).color(Color.BLACK)
                                        .alpha((int) (255 * 0.55)));
                        showOrigin.setVisible(true);
                        View actionView = findViewById(showOrigin.getItemId());
                        showOrigin.setOnMenuItemClickListener(item -> {
                            try {
                                if (mOriginImage == null) {
                                    mOriginImage = new ImageView(mContext);
                                    mOriginImage.setImageDrawable(info.getIcon());

                                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                                            (int) mContext.getResources().getDimension(
                                                    R.dimen.dialog_icon_size),
                                            (int) mContext.getResources().getDimension(
                                                    R.dimen.dialog_icon_size)
                                    );
                                    mOriginImage.setLayoutParams(params);
                                    mOriginImage.setPadding(dip2px(mContext, 8), 0, 0, 0);
                                }
                                if (mRoot.getChildCount() > 1) {
                                    mRoot.removeView(mOriginImage);
                                    if (actionView != null) {
                                        actionView.setAlpha(1);
                                    }
                                } else {
                                    mRoot.addView(mOriginImage);
                                    if (actionView != null) {
                                        actionView.setAlpha(0.5f);
                                    }
                                }
                                return true;
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                            return false;
                        });
                        if (actionView != null) {
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                            alphaAnimation.setDuration(300);
                            actionView.startAnimation(alphaAnimation);
                        }
                    }
                }, 100), Timber::e);
    }

    private void showRealName(String name) {
        mTitleTextView.setPivotX(0);
        mTitleTextView.setPivotY(1);

        mTitleTextView.animate()
                .alpha(0.6f)
                .scaleY(0.6f)
                .scaleX(0.6f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        TextView textView = new TextView(mContext);
                        textView.setTextColor(
                                ResourceUtil.getColor(mContext, R.color.title));
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        textView.setText(name);
                        LayoutParams layoutParams = new LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, dip2px(mContext, 8), 0, 0);
                        textView.setLayoutParams(layoutParams);
                        mTitleContainer.addView(textView, 0);
                        mTitleContainer.requestLayout();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        Timber.d("canceled");
                    }

                    @Override
                    public void onAnimationPause(Animator animation) {
                        super.onAnimationPause(animation);
                        Timber.d("paused");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mTitleTextView.requestLayout();
                    }
                })
                .setDuration(300)
                .start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private Menu mMenu;

    private boolean hasInitMenu = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu == null) {
            return false;
        }
        mMenu = menu;
        if (mComponent != null && !hasInitMenu) {
            hasInitMenu = true;
            getMenuInflater().inflate(R.menu.menu_icon_dialog, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_in_store) {
            final String appPackageName = StringUtil.componentInfoToPackageName(mComponent);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)));
                onBackPressed();
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id="
                                + appPackageName)));
            }
        }
//        else if (id == R.id.action_show_origin_icon) {
//            try {
//                if (mOriginImage == null) {
//                    mOriginImage = new ImageView(mContext);
//                    mOriginImage.setImageDrawable(mDisplayInfo.getIcon());
//
//                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                            (int) mContext.getResources()
//                                    .getDimension(R.dimen.dialog_icon_size),
//                            (int) mContext.getResources().getDimension(R.dimen.dialog_icon_size)
//                    );
//                    mOriginImage.setLayoutParams(params);
//                    mOriginImage.setPadding(dip2px(mContext, 8), 0, 0, 0);
//                }
//                if (mRoot.getChildCount() > 1) {
//                    mRoot.removeView(mOriginImage);
//                } else {
//                    mRoot.addView(mOriginImage);
//                }
//            } catch (Exception e) {
//                Timber.e(e);
//            }
//        }
        else if (id == R.id.action_create_shortcut) {
            Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcutIntent.putExtra("duplicate", false);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, mLabel);
            Intent.ShortcutIconResource ico = Intent.ShortcutIconResource
                    .fromContext(getApplicationContext(), R.mipmap.ic_launcher);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ico);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            sendBroadcast(shortcutIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
