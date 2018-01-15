package com.sorcerer.sorcery.iconpack.apply.database.base;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/10
 */

public abstract class BaseApplyActivity extends BaseActivity {
    protected static final String ACTION_APPLY = "action_apply";
    protected static final String ACTION_RESTORE = "action_restore";

    @Override
    protected int provideLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        String action = getIntent().getAction();
        if (ACTION_APPLY.equals(action)) {
            apply();
        } else if (ACTION_RESTORE.equals(action)) {
            restore();
        }
    }

    protected abstract String packageName();

    protected abstract String componentName();

    protected abstract ILauncherApplier applier();

    protected Observable<List<String>> doApply() {
        return applier().apply();
    }

    protected void apply() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean grant) throws Exception {
                        if (!grant) {
                            throw new Exception(ResourceUtil.getString(mActivity,
                                    R.string.apply_by_database_no_sdcard_permission));
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.newThread())
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean aBoolean) throws Exception {
                        return doApply();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    private Dialog mDialog;

                    @Override
                    public void onSubscribe(Disposable d) {
                        onApplyStart();

                        mDialog = Dialogs
                                .indeterminateProgressDialog(mActivity, ResourceUtil
                                        .getString(mActivity, R.string.apply_by_database_applying));
                        mDialog.setCancelable(false);
                        mDialog.show();
                    }

                    @Override
                    public void onNext(List<String> list) {
                        onApplyNext();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onApplyError();

                        Timber.e(e);
                        mDialog.dismiss();
                        Toast.makeText(mActivity,
                                ResourceUtil.getString(mActivity, R.string.apply_by_database_fail)
                                        + "\n" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        doFinish();
                    }

                    @Override
                    public void onComplete() {
                        onApplyComplete();

                        mDialog.dismiss();
                        Toast.makeText(mActivity, R.string.apply_by_database_success,
                                Toast.LENGTH_SHORT)
                                .show();

                        mActivity.getWindow().getDecorView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.setComponent(new ComponentName(
                                        packageName(),
                                        componentName()));
                                startActivity(intent);
                            }
                        }, 500);

                        doFinish();
                    }
                });
    }

    protected Observable<List<String>> doRestore() {
        return applier().restore();
    }

    protected void restore() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean grant) throws Exception {
                        if (!grant) {
                            throw new Exception(ResourceUtil.getString(mActivity,
                                    R.string.apply_by_database_no_sdcard_permission));
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.newThread())
                .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(Boolean aBoolean) throws Exception {
                        return doRestore();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    private Dialog mDialog;

                    @Override
                    public void onSubscribe(Disposable d) {
                        onRestoreStart();

                        mDialog = Dialogs
                                .indeterminateProgressDialog(mActivity, ResourceUtil
                                        .getString(mActivity, R.string.apply_by_database_applying));
                        mDialog.setCancelable(false);
                        mDialog.show();
                    }

                    @Override
                    public void onNext(List<String> list) {
                        onRestoreNext();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onRestoreError();

                        Timber.e(e);
                        mDialog.dismiss();
                        Toast.makeText(mActivity,
                                ResourceUtil.getString(mActivity, R.string.apply_by_database_fail)
                                        + "\n" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        onRestoreComplete();

                        mDialog.dismiss();
                        Toast.makeText(mActivity, R.string.apply_by_database_success,
                                Toast.LENGTH_SHORT).show();
                        mActivity.getWindow().getDecorView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(
                                        Intent.ACTION_MAIN);
                                intent.setComponent(new ComponentName(
                                        packageName(),
                                        componentName()));
                                startActivity(intent);
                            }
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

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    protected void onApplyStart() {
    }

    protected void onApplyNext() {
    }

    protected void onApplyError() {
    }

    protected void onApplyComplete() {
    }

    protected void onRestoreStart() {
    }

    protected void onRestoreNext() {
    }

    protected void onRestoreError() {
    }

    protected void onRestoreComplete() {
    }

//    @Override
//    protected ViewGroup rootView() {
//        return null;
//    }
}
