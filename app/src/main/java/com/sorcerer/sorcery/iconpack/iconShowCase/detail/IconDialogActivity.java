package com.sorcerer.sorcery.iconpack.iconShowCase.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialize.util.UIUtils;
import com.sorcerer.sorcery.iconpack.MainActivity;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.ToolbarActivity;
import com.sorcerer.sorcery.iconpack.utils.LocaleUtil;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.StringUtil;
import com.sorcerer.sorcery.iconpack.utils.TextWeightUtil;
import com.sorcerer.sorcery.iconpack.utils.ViewUtil;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon.gmd_compare;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/3/22 0022
 */

public class IconDialogActivity extends ToolbarActivity {
    public static final String EXTRA_RES = "EXTRA_RES";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_LABEL = "EXTRA_LABEL";
    public static final String EXTRA_LESS_ANIM = "EXTRA_LESS_ANIM";
    @BindView(R.id.toolbar_icon_dialog)
    Toolbar mToolbar;
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
    ViewGroup mTitleContainer;
    private String mLabel;
    private String mName;
    private int mRes;
    private String mComponent;
    private String mPackageName;

    private ImageView mOriginImage;

    private boolean mLessAnim = false;
    private Menu mMenu;
    private MenuItem mShowOriginMenuItem;
    private boolean hasInitMenu = false;

    @Override
    protected int provideLayoutId() {
        return R.layout.dialog_icon_show;
    }

    @Override
    protected void hookBeforeSetContentView() {
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLabel = getIntent().getStringExtra(EXTRA_LABEL);
        mName = getIntent().getStringExtra(EXTRA_NAME);
        mRes = getIntent().getIntExtra(EXTRA_RES, 0);
        mLessAnim = getIntent().getBooleanExtra(EXTRA_LESS_ANIM, false);

        if (mRes == 0) {
            this.finish();
        }

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ResourceUtil.getColor(this, R.color.palette_transparent)));
        }

        mTitleTextView.setText(mLabel);
        TextWeightUtil.INSTANCE.medium(mTitleTextView);

        mIconImageView.setImageResource(mRes);

        mBackground.setOnTouchListener((v, event) -> {
            if (!ViewUtil.INSTANCE.isPointInsideView(event.getX(),
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
        getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                showDetail();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

//    @Override
//    protected ViewGroup rootView() {
//        return null;
//    }

    private void showDetail() {
        Observable.just(mName)
                .observeOn(Schedulers.newThread())
                .map(name -> PackageUtil.getComponentByName(mContext, name))
                .filter(s -> !TextUtils.isEmpty(s))
                .flatMap(component -> {
                    mComponent = component;
                    mPackageName = StringUtil.INSTANCE.componentInfoToPackageName(mComponent);
                    Timber.d(mPackageName);
                    return AppDisplayInfoGetter
                            .getAppDisplayInfo(mPackageName, IconDialogActivity.this,
                                    LocaleUtil.INSTANCE.isChinese(IconDialogActivity.this));
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
//                            Glide.with(IconDialogActivity.this)
//                                    .load(info.getIconUrl()).into()
                            Timber.d(info.getIconUrl());
                            info.setIcon(Glide.with(IconDialogActivity.this)
                                    .load(info.getIconUrl())
                                    .submit()
                                    .get());
//                            info.setIcon(Glide.with(IconDialogActivity.this)
//                                    .load(info.getIconUrl())
//                                    .get());
                        } catch (InterruptedException | ExecutionException e) {
                            Timber.e(e);
                        }
                    }
                    return info;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(info -> mTitleTextView.postDelayed(() -> {
                    if (mMenu != null && info.getIcon() != null && mShowOriginMenuItem != null) {
                        View actionView = findViewById(mShowOriginMenuItem.getItemId());
                        mShowOriginMenuItem.setOnMenuItemClickListener(item -> {
                            try {
                                if (mOriginImage == null) {
                                    mOriginImage = new ImageView(mContext);
                                    mOriginImage.setImageDrawable(info.getIcon());

                                    ViewGroup.LayoutParams params =
                                            new ViewGroup.LayoutParams(
                                                    (int) mContext.getResources()
                                                            .getDimension(
                                                                    R.dimen.dialog_icon_size),
                                                    (int) mContext.getResources()
                                                            .getDimension(
                                                                    R.dimen.dialog_icon_size)
                                            );
                                    mOriginImage.setLayoutParams(params);
                                    mOriginImage.setPadding(
                                            (int) UIUtils.convertDpToPixel(8, mContext),
                                            0, 0, 0);
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
                        mShowOriginMenuItem.setCheckable(true);
                        if (actionView != null) {
                            actionView.setAlpha(1);
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                            alphaAnimation.setDuration(300);
                            actionView.startAnimation(alphaAnimation);
                        }
                    }
                }, 100), Timber::e);
    }

    private void showRealName(String name) {
        mTitleTextView.setPivotX(0);
        mTitleTextView.setPivotY(0);

        mTitleTextView.animate()
                .alpha(0.5f)
                .scaleY(0.6f)
                .scaleX(0.6f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        TextView textView = new TextView(mContext);
                        TextWeightUtil.INSTANCE.medium(textView);
                        textView.setTextColor(ResourceUtil.getAttrColor(mContext,
                                android.R.attr.textColorPrimary));
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        textView.setText(name);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.ABOVE, mTitleTextView.getId());
                        layoutParams
                                .setMargins(0, (int) UIUtils.convertDpToPixel(8, mContext), 0, 0);
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
                        mTitleContainer.requestLayout();
                        mTitleContainer.forceLayout();
                        mTitleContainer.invalidate();
                    }
                })
                .setDuration(300)
                .start();
    }

    @Override
    protected void onPause() {
        if (mLessAnim) {
            overridePendingTransition(0, 0);
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK, getIntent());
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu == null) {
            return false;
        }
        mMenu = menu;
        if (mComponent != null && !hasInitMenu) {
            hasInitMenu = true;
            getMenuInflater().inflate(R.menu.menu_icon_dialog, menu);
            mShowOriginMenuItem = menu.findItem(R.id.action_show_origin_icon);
            mShowOriginMenuItem.setCheckable(false);
            findViewById(mShowOriginMenuItem.getItemId()).setAlpha(0);
            mShowOriginMenuItem.setIcon(new IconicsDrawable(mContext, gmd_compare)
                    .sizeDp(24).paddingDp(2)
                    .color(ResourceUtil
                            .getAttrColor(mContext, android.R.attr.textColorSecondary))
            );
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_in_store) {
            final String appPackageName =
                    StringUtil.INSTANCE.componentInfoToPackageName(mComponent);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                onBackPressed();
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id="
                                + appPackageName))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } else if (id == R.id.action_create_shortcut) {
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
