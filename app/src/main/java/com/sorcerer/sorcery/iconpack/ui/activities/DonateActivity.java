package com.sorcerer.sorcery.iconpack.ui.activities;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SorceryIcons;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.ui.views.QCardView;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.OnClick;

public class DonateActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isDonated = SorceryIcons.getPrefs().donated().getValue();

        if (isDonated) {
            mThankCard.post(new Runnable() {
                @Override
                public void run() {
                    showThanksCard();
                }
            });
        }
    }

    @OnClick(R.id.button_donate_alipay)
    void onAlipayClick() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(
                "alipayqr://platformapi/startapp?"
                        + "saId=10000007"
                        + "&qrcode=https%3A%2F%2Fqr.alipay.com%2Fapx04314ky3hnfqt9xuaze3"));
        intent.setPackage("com.eg.android.AlipayGphone");
        try {

            startActivity(intent);
            SorceryIcons.getPrefs().donated().setValue(true);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mActivity, "no alipay", Toast.LENGTH_SHORT).show();
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.button_donate_play)
    void onPlayClick() {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
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

}
