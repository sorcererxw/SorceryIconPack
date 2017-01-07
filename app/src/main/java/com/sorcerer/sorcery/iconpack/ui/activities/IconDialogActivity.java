package com.sorcerer.sorcery.iconpack.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.net.spiders.AppNameGetter;
import com.sorcerer.sorcery.iconpack.ui.activities.base.ToolbarActivity;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.DisplayUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.sorcerer.sorcery.iconpack.utils.StringUtil;
import com.sorcerer.sorcery.iconpack.utils.ViewUtil;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }
    }

    @Override
    protected Toolbar provideToolbar() {
        return mToolbar;
    }

    @Override
    protected void init() {

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

        mBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!ViewUtil.isPointInsideView(event.getX(),
                        event.getY(),
                        findViewById(R.id.cardView_icon_dialog_card))) {
                    onBackPressed();
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Observable.just(mName).subscribeOn(Schedulers.newThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String name) {
                        String res = PackageUtil.getComponentByName(IconDialogActivity.this, name);
                        if (res == null) {
                            res = "";
                        }
                        return res;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(final String component) {
                        mComponent = component;
                        mPackageName = StringUtil.componentInfoToPackageName(mComponent);
                        showRealName(mPackageName);
                        if (mMenu != null) {
                            onCreateOptionsMenu(mMenu);
                        }
                        if (false && BuildConfig.DEBUG) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    TextView componentTextView =
                                            (TextView) View.inflate(IconDialogActivity.this,
                                                    R.layout.textview_dialog_icon_component_info,
                                                    null);
                                    componentTextView.setVisibility(View.VISIBLE);
                                    componentTextView.setText(
                                            (component == null || component.isEmpty()) ?
                                                    "null" : component);
                                    mComponentContainer.addView(componentTextView);
                                }
                            }, 500);
                        }
                    }
                });
    }

    private void showRealName(String packageName) {
        if (packageName == null) {
            return;
        }
        Timber.d(packageName);
        AppNameGetter.getName(packageName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(s -> s != null && !s.isEmpty())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final String appName) {
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
                                        textView.setText(appName);
                                        LayoutParams layoutParams = new LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins(0, dip2px(mContext, 8), 0, 0);
                                        textView.setLayoutParams(layoutParams);
                                        mTitleContainer.addView(textView, 0);
                                        mTitleContainer.requestLayout();
                                    }
                                })
                                .start();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < 21) {
            overridePendingTransition(0, R.anim.fast_fade_out);
        }
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
        mMenu = menu;
        if (mComponent != null && !hasInitMenu) {
            hasInitMenu = true;
            getMenuInflater().inflate(R.menu.menu_icon_dialog, menu);

            MenuItem showOrigin = menu.findItem(R.id.action_show_origin_icon);
            if (PackageUtil.isPackageInstalled(mContext,
                    StringUtil.componentInfoToPackageName(mComponent))) {
                showOrigin.setVisible(true);
                Drawable icon = showOrigin.getIcon();
                icon.setAlpha((int) (255 * 0.5));
                showOrigin.setIcon(icon);
            } else {
                showOrigin.setVisible(false);
            }
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
        } else if (id == R.id.action_show_origin_icon) {
            try {
                if (mOriginImage == null) {
                    mOriginImage = new ImageView(mContext);
                    mOriginImage.setImageDrawable(
                            getPackageManager().getApplicationIcon(
                                    StringUtil.componentInfoToPackageName(mComponent)
                            )
                    );

                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            (int) mContext.getResources()
                                    .getDimension(R.dimen.dialog_icon_size),
                            (int) mContext.getResources().getDimension(R.dimen.dialog_icon_size)
                    );
                    mOriginImage.setLayoutParams(params);
                    mOriginImage.setPadding(dip2px(mContext, 8), 0, 0, 0);
                }
                if (mRoot.getChildCount() > 1) {
                    mRoot.removeView(mOriginImage);
                } else {
                    mRoot.addView(mOriginImage);
                }
            } catch (Exception e) {
                Timber.e(e);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
