package com.sorcerer.sorcery.iconpack.ui.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.views.QCardView;
import com.sorcerer.sorcery.iconpack.ui.views.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.util.Utility;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import c.b.BP;

public class DonateActivity extends SlideInAndOutAppCompatActivity implements View.OnClickListener {

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        new Thread(new Runnable() {
            @Override
            public void run() {
                BP.init(getApplication(), getString(R.string.bmob_app_id));
            }
        }).start();

        mActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_universal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        findViewById(R.id.button_donate_alipay).setOnClickListener(this);
        findViewById(R.id.button_donate_wechat).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("sorcery icon pack",
                MODE_PRIVATE);
        boolean isDonated = sharedPreferences.getBoolean("is_donated", false);
        if (isDonated) {
            showThanksCard();
        }
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
                    pay(seekBar.getProgress(), isAlipay);
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

    private void pay(int money, boolean isAlipay) {
        BP.pay(mActivity, "捐赠", "感谢捐赠:)", money, isAlipay, new c.b.PListener() {

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
//        cardView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        cardView.animate().
//                        cardView.setY(event.getY());
//                        break;
//                }
//                return false;
//            }
//        });
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
}
