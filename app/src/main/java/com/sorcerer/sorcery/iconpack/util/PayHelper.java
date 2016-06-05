package com.sorcerer.sorcery.iconpack.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;

import c.b.BP;
import c.b.PListener;

/**
 * Created by pqylj on 2016/5/15.
 */
public class PayHelper {
    private static final String TAG = "PayHelper";

    private Activity mContext;

    private PayCallback mPayCallback;

    public interface PayCallback {
        void onSuccess(String orderId);

        void onFail();
    }

    public PayHelper(Activity context) {
        mContext = context;
    }

    public void pay(final boolean isAlipay, int amount, String title, String describe) {

        if (!isAlipay && !AppInfoUtil.isPackageInstalled(mContext, "com.bmob.app.sport")) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
            builder.content(ResourceUtil.getString(mContext, R.string.wechat_pay_plugin_content));
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog,
                        @NonNull DialogAction which) {
                    ApkUtil.installApkFromAssets(dialog.getContext(), "BmobPayPlugin.apk");
                }
            });
            builder.positiveText(ResourceUtil.getString(mContext, R.string.action_install));
            builder.negativeText(ResourceUtil.getString(mContext, R.string.cancel));
            builder.show();
        } else {
            BP.pay(title, describe, amount, isAlipay, new PListener() {
                private String mOrderid;

                @Override
                public void orderId(String s) {
                    Toast.makeText(mContext,
                            mContext.getString(
                                    isAlipay ? R.string.open_alipay : R.string.open_wechat),
                            Toast.LENGTH_LONG)
                            .show();
                    mOrderid = s;
                }

                @Override
                public void succeed() {
                    if (mPayCallback != null) {
                        mPayCallback.onSuccess(mOrderid);
                    }
                }

                @Override
                public void fail(int i, String s) {
                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                    Log.d(TAG, i + " " + s);
                    if (mPayCallback != null) {
                        mPayCallback.onFail();
                    }
                }

                @Override
                public void unknow() {
                    Log.d(TAG, "unknow");
                }
            });
        }
    }

    public void setPayCallback(PayCallback payCallback) {
        mPayCallback = payCallback;
    }
}
