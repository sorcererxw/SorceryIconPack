package com.sorcerer.sorcery.iconpack.ui.activities;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.views.QCardView;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import c.b.BP;

public class DonateActivity extends SlideInAndOutAppCompatActivity implements View.OnClickListener {

    private Activity mActivity;
    private int mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BP.init(getApplication(), getString(R.string.bmob_app_id));
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, "fail to load service", Toast.LENGTH_SHORT)
                                    .show();
                            findViewById(R.id.button_donate_alipay).setEnabled(false);
                        }
                    });
                }
            }
        }).start();

        mActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_universal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.button_donate_alipay).setOnClickListener(this);
        findViewById(R.id.button_donate_wechat).setOnClickListener(this);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_donate);
        QCardView cardView = (QCardView) findViewById(R.id.cardView_donate_thank);
        cardView.setTouchCallBack(new QCardView.TouchCallBack() {
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
        boolean isAlipay = id == R.id.button_donate_alipay ? true : false;
        showMoneySelectDialog(isAlipay);
    }

    private void showMoneySelectDialog(final boolean isAlipay) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(getString(R.string.donate_dialog_title));
        View view = LayoutInflater.from(this).inflate(R.layout.layout_donate_money_select, null);
        final DiscreteSeekBar seekBar = (DiscreteSeekBar) view.findViewById(R.id
                .discreteSeekBar_donate_money_select);
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
                } else if (which == DialogAction.NEGATIVE) {
                    Snackbar.make(findViewById(R.id.linearLayout_donate_docker),
                            getString(R.string.donate_success),
                            Snackbar.LENGTH_SHORT);
                }
            }
        });
        builder.positiveText(getString(R.string.ok));
        builder.negativeText(getString(R.string.cancel));
        builder.show();
    }

    private void pay(boolean isAlipay) {
        BP.pay(mActivity, "捐赠", "感谢捐赠:)", mAmount, isAlipay, new c.b.PListener() {

            @Override
            public void orderId(String s) {
                Toast.makeText(mActivity, getString(R.string.open_alipay), Toast.LENGTH_LONG)
                        .show();
                Log.d("sip donate", "order" + "\n" + s);
            }

            @Override
            public void succeed() {
                Snackbar.make(findViewById(R.id.recyclerView_donate_root), getString(R.string
                        .donate_success), Snackbar
                        .LENGTH_SHORT);
                showThanksCard();
                SharedPreferences sharedPreferences = getSharedPreferences("sorcery icon pack",
                        MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("is_donated", true).apply();
            }

            @Override
            public void fail(int i, String s) {
                Snackbar.make(findViewById(R.id.recyclerView_donate_root), getString(R.string
                        .donate_fail), Snackbar
                        .LENGTH_LONG);
                Log.d("sip donate", "fail\n " + i + "\n" + s);
            }

            @Override
            public void unknow() {
                Log.d("sip donate", "unknow");
            }
        });
    }

    private void showThanksCard() {
        final QCardView cardView = (QCardView) findViewById(R.id.cardView_donate_thank);
        if (cardView.getVisibility() == View.VISIBLE) {
            return;
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        cardView.setTranslationX(w_screen);

        cardView.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                cardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                findViewById(R.id.imageView_donate_heart).setVisibility(View.VISIBLE);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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
