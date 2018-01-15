package com.sorcerer.sorcery.iconpack;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;
import com.sorcerer.sorcery.iconpack.ui.views.QCardView;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class DonateActivity extends BaseSubActivity {

    @BindView(R.id.button_donate_bitcoin)
    Button mBitcoinButton;
    @BindView(R.id.button_donate_alipay)
    Button mAlipayButton;
    @BindView(R.id.button_donate_play)
    Button mPlayButton;
    @BindView(R.id.scrollView_donate)
    ScrollView mScrollView;
    @BindView(R.id.cardView_donate_thank)
    QCardView mThankCard;
    @BindView(R.id.imageView_donate_heart)
    ImageView mHeart;
    @BindView(R.id.imageView_donate_card)
    ImageView mCardImage;
    @BindView(R.id.coordinatorLayout_donate)
    CoordinatorLayout mCoordinatorLayout;
//    private IInAppBillingService mService;
//    private ServiceConnection mServiceConnection;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isDonated = mPrefs.donated().getValue();

        if (isDonated) {
            mThankCard.post(this::showThanksCard);
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
            mPrefs.donated().setValue(true);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "no alipay", Toast.LENGTH_SHORT).show();
            Timber.e(e);
        }
    }

    @OnClick(R.id.button_donate_bitcoin)
    void onBitcoinClick() {

    }

    @OnClick(R.id.button_donate_play)
    void onPlayClick() {
//        try {
//            ArrayList<String> skuList = new ArrayList<>();
//            Bundle querySkus = new Bundle();
//            querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
//            Bundle skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
//            if (skuDetails.getInt("RESPONSE_CODE") == 0) {
//                ArrayList<String> responseList
//                        = skuDetails.getStringArrayList("DETAILS_LIST");
//
//                assert responseList != null;
//                for (String thisResponse : responseList) {
//                    JSONObject object = new JSONObject(thisResponse);
//                    String sku = object.getString("productId");
//                    String price = object.getString("price");
//                    Timber.d(sku + " : " + price);
////                    if (sku.equals("premiumUpgrade")) {
////                        mPremiumUpgradePrice = price;
////                    } else if (sku.equals("gas")) {
////                        mGasPrice = price;
////                    }
//                }
//            }
//        } catch (RemoteException | JSONException e) {
//            e.printStackTrace();
//        }
////        try {
////            Bundle buyIntentBundle =
////                    mService.getBuyIntent(3, getPackageName(), "donate_1.99", "inapp", "");
////        } catch (RemoteException e) {
////            e.printStackTrace();
////        }
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_donate;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarBackIndicator();

        if (PackageUtil.isAlipayInstalled(this)) {
            mAlipayButton.setVisibility(View.VISIBLE);
        }
        if (PackageUtil.isInstallFromPlay(this) && BuildConfig.DEBUG) {
//            mPlayButton.setVisibility(View.VISIBLE);
//            mServiceConnection = new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName name, IBinder service) {
//                    mService = IInAppBillingService.Stub.asInterface(service);
//                }
//
//                @Override
//                public void onServiceDisconnected(ComponentName name) {
//                    mService = null;
//                }
//            };
//
//            Intent serviceIntent =
//                    new Intent("com.android.vending.billing.InAppBillingService.BIND");
//            serviceIntent.setPackage("com.android.vending");
//            bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
        if (PackageUtil.isCryptoAppInstalled(this)) {
            mBitcoinButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mService != null) {
//            unbindService(mServiceConnection);
//        }
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

        Glide.with(mContext)
                .load(R.drawable.gnarly_90s)
                .apply(RequestOptions.noAnimation())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource,
                                                Transition<? super Drawable> transition) {
                        mCardImage.setImageDrawable(resource);

                        mThankCard.animate().setListener(new Animator.AnimatorListener() {
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
                        }).setDuration(1000).translationX(0).start();
                    }
                });
    }


}
