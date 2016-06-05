package com.sorcerer.sorcery.iconpack.ui.activities;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SIP;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.ui.views.QCardView;
import com.sorcerer.sorcery.iconpack.util.PayHelper;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.BindView;
import butterknife.OnClick;

public class DonateActivity extends SlideInAndOutAppCompatActivity {

    private int mAmount;

    @OnClick(R.id.button_donate_alipay)
    void onAlipayClick() {
        if (false) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(
                    "alipayqr://platformapi/startapp?saId=10000007&qrcode=https%3A%2F"
                            + "%2Fqr.alipay.com%2F"
                            + "apx04314ky3hnfqt9xuaze3"));
            intent.setPackage("com.eg.android.AlipayGphone");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mActivity, "no alipay", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            showMoneySelectDialog(true);
        }
    }

    @OnClick(R.id.button_donate_wechat)
    void onWechatClick() {
        showMoneySelectDialog(false);
    }

    @BindView(R.id.scrollView_donate)
    ScrollView mScrollView;

    @BindView(R.id.cardView_donate_thank)
    QCardView mThankCard;

    @BindView(R.id.imageView_donate_heart)
    ImageView mHeart;

    @BindView(R.id.imageView_donate_card)
    ImageView mCardImage;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_donate;
    }

    @Override
    protected void init() {
        super.init();

        setToolbarBackIndicator();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("sorcery icon pack",
                MODE_PRIVATE);

        boolean isDonated = sharedPreferences.getBoolean("is_donated", false);
        if (isDonated || SIP.DEBUG) {
            mThankCard.post(new Runnable() {
                @Override
                public void run() {
                    showThanksCard();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

    private void showMoneySelectDialog(final boolean isAlipay) {
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(getString(R.string.donate_dialog_title));
        View view = LayoutInflater.from(this).inflate(R.layout.layout_donate_money_select, null);
        final DiscreteSeekBar seekBar = (DiscreteSeekBar) view.findViewById(R.id
                .discreteSeekBar_donate_money_select);
        seekBar.setProgress(6);
        builder.customView(view, true);
        builder.onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    mAmount = seekBar.getProgress();
                    if (Build.VERSION.SDK_INT >= 23 && !PermissionsHelper.hasPermission(mActivity,
                            PermissionsHelper.READ_PHONE_STATE_MANIFEST)) {
                        PermissionsHelper.requestReadPhoneState(mActivity);
                    } else {
                        pay(isAlipay);
                    }
                }
            }
        });
        builder.positiveText(getString(R.string.ok));
        builder.negativeText(getString(R.string.cancel));
        builder.show();
    }

    private void pay(final boolean isAlipay) {
        PayHelper payHelper = new PayHelper(mActivity);
        payHelper.setPayCallback(new PayHelper.PayCallback() {
            @Override
            public void onSuccess(String orderId) {

                Toast.makeText(mContext,
                        ResourceUtil.getString(mContext, R.string.donate_success),
                        Toast.LENGTH_LONG).show();

                showThanksCard();
                SharedPreferences sharedPreferences = getSharedPreferences("sorcery icon pack",
                        MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("is_donated", true).apply();
            }

            @Override
            public void onFail() {
            }
        });
        payHelper.pay(isAlipay, mAmount, "捐赠", "感谢捐赠");
    }

    private void showThanksCard() {
        if (mThankCard.getVisibility() == View.VISIBLE) {
            return;
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        mThankCard.setTranslationX(w_screen);

        mThankCard.setTouchCallBack(new QCardView.TouchCallBack() {
            @Override
            public void onDown() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onUp() {
                mScrollView.requestDisallowInterceptTouchEvent(false);
            }
        });


        Glide.with(mContext).load(R.drawable.gnarly_90s)
                .asBitmap()
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                            GlideAnimation<? super Bitmap> glideAnimation) {
                        mCardImage.setImageBitmap(resource);

                        mThankCard.animate()
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        mThankCard.setVisibility(View.VISIBLE);
                                        mThankCard.setTouchable(false);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mHeart.setVisibility(View.VISIBLE);
                                        mThankCard.setTouchable(true);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {
                                    }
                                })
                                .setDuration(1000)
                                .translationX(0)
                                .start();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == PermissionsHelper.READ_PHONE_STATE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pay(true);
            } else {
                Toast.makeText(mActivity, "no permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
