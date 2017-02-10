package com.sorcerer.sorcery.iconpack.apply.appliers.database.smartisan;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/10
 */

public class SmartisanApplyActivity extends AppCompatActivity {
    protected static final String ACTION_APPLY = "action_apply";
    protected static final String ACTION_RESTORE = "action_restore";

    public static void apply(Context context, boolean apply) {
        Intent intent = new Intent(context, SmartisanApplyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(apply ? ACTION_APPLY : ACTION_RESTORE);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    private Activity mActivity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = getIntent().getAction();

        if (action.equals(ACTION_APPLY)) {
            apply();
        } else if (action.equals(ACTION_RESTORE)) {
            restore();
        }
    }

    private void apply() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(grant -> {
                    if (!grant) {
                        throw new Exception(ResourceUtil.getString(mActivity,
                                R.string.apply_by_database_no_sdcard_permission));
                    }
                    return true;
                })
                .observeOn(Schedulers.newThread())
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean aBoolean) throws Exception {
                        return new SmartisanLauncherApplier(mActivity).apply();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    private ProgressDialog mDialog;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDialog = new ProgressDialog(mActivity);
                        mDialog.setMessage(ResourceUtil
                                .getString(mActivity, R.string.apply_by_database_applying));
                        mDialog.setCancelable(false);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                    }

                    @Override
                    public void onNext(List<String> list) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        mDialog.dismiss();
                        Toast.makeText(mActivity,
                                ResourceUtil.getString(mActivity, R.string.apply_by_database_fail)
                                        + "\n" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        mDialog.dismiss();
                        Toast.makeText(mActivity, R.string.apply_by_database_success,
                                Toast.LENGTH_SHORT)
                                .show();

                        mActivity.getWindow().getDecorView().postDelayed(() -> {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setComponent(new ComponentName(
                                    "com.smartisanos.home",
                                    "com.smartisanos.home.Launcher"));
                            startActivity(intent);
                        }, 500);

                        doFinish();
                    }
                });
    }

    private void restore() {
        new SmartisanLauncherApplier(mActivity).restore()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    private ProgressDialog mDialog;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDialog = new ProgressDialog(mActivity);
                        mDialog.setMessage(ResourceUtil
                                .getString(mActivity, R.string.apply_by_database_applying));
                        mDialog.setCancelable(false);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                    }

                    @Override
                    public void onNext(List<String> list) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        mDialog.dismiss();
                        Toast.makeText(mActivity,
                                ResourceUtil.getString(mActivity, R.string.apply_by_database_fail)
                                        + "\n" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        mDialog.dismiss();
                        Toast.makeText(mActivity, R.string.apply_by_database_success,
                                Toast.LENGTH_SHORT)
                                .show();
                        mActivity.getWindow().getDecorView()
                                .postDelayed(() -> {
                                    Intent intent = new Intent(
                                            Intent.ACTION_MAIN);
                                    intent.setComponent(new ComponentName(
                                            "com.smartisanos.home",
                                            "com.smartisanos.home.Launcher"));
                                    startActivity(intent);
                                }, 500);

                        doFinish();
                    }
                });
    }

    protected void doFinish() {
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
