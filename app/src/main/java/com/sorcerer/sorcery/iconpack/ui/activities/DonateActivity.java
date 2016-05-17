package com.sorcerer.sorcery.iconpack.ui.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SIP;
import com.sorcerer.sorcery.iconpack.databinding.ActivityDonateBinding;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.ui.views.QCardView;
import com.sorcerer.sorcery.iconpack.util.ApkUtil;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.util.PayHelper;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.Utility;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import c.b.BP;

public class DonateActivity extends SlideInAndOutAppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DonateActivity";
    private Activity mActivity;
    private int mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDonateBinding binding = DataBindingUtil.setContentView(this, R.layout
                .activity_donate);

        mActivity = this;

        setSupportActionBar(binding.include.toolbarUniversal);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.layoutButtonDonates.setAlipayListener(this);
        binding.layoutButtonDonates.setWechatListener(this);

        final ScrollView scrollView = binding.scrollViewDonate;

        binding.cardViewDonateThank.setTouchCallBack(new QCardView.TouchCallBack() {
            @Override
            public void onDown() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onUp() {
                scrollView.requestDisallowInterceptTouchEvent(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("sorcery icon pack",
                MODE_PRIVATE);

        boolean isDonated = sharedPreferences.getBoolean("is_donated", false);
        if (isDonated) {
            showThanksCard();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_donate_alipay) {

            if (true) {
                Intent intent = new Intent("android.intent.action.VIEW",
                        Uri.parse(
                                "alipayqr://platformapi/startapp?saId=10000007&qrcode=https%3A%2F" +
                                        "%2Fqr.alipay.com%2F" + "apx04314ky3hnfqt9xuaze3"));
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
        } else if (id == R.id.button_donate_wechat) {
            showMoneySelectDialog(false);
        }
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
                    if (Build.VERSION.SDK_INT >= 23 && !PermissionsHelper.hasPermission
                            (mActivity, PermissionsHelper.READ_PHONE_STATE_MANIFEST)) {
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
                Snackbar.make(findViewById(R.id.recyclerView_donate_root), getString(R.string
                        .donate_success), Snackbar
                        .LENGTH_SHORT);
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
        final QCardView cardView = (QCardView) findViewById(R.id.cardView_donate_thank);
        assert cardView != null;
        if (cardView.getVisibility() == View.VISIBLE) {
            return;
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        cardView.setTranslationX(w_screen);

        ImageLoader.getInstance().displayImage(
                "drawable://" +
                        getResources()
                                .getIdentifier("gnarly_90s", "drawable", getPackageName()),
                (ImageView) findViewById(R.id.imageView_donate_card),
                SIP.mOptions, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        cardView.animate().setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                cardView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                ImageView heart =
                                        (ImageView) findViewById(R.id.imageView_donate_heart);
                                heart.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(
                                        "drawable://" + getResources().getIdentifier("heart",
                                                "drawable",
                                                getPackageName()), heart);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).setDuration(1000).translationX(0).start();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

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
