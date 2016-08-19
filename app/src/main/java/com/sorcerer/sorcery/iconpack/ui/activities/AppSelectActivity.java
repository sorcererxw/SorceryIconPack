package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.net.leancloud.RequestBean;
import com.sorcerer.sorcery.iconpack.ui.activities.base.UniversalToolbarActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.NewRequestAdapter;
import com.sorcerer.sorcery.iconpack.ui.views.MyFloatingActionButton;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.ToolbarOnGestureListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AppSelectActivity extends UniversalToolbarActivity {

    @BindView(R.id.recyclerView_app_select)
    RecyclerView mRecyclerView;

    @BindView(R.id.avLoadingIndicatorView_icon_select)
    AVLoadingIndicatorView mIndicatorView;

    @BindView(R.id.fab_app_select)
    MyFloatingActionButton mFAB;

    @OnClick(R.id.fab_app_select)
    void onFABClick() {
        if (PermissionsHelper.hasPermission(this, PermissionsHelper.READ_PHONE_STATE_MANIFEST)
                && PermissionsHelper
                .hasPermission(this, PermissionsHelper.WRITE_EXTERNAL_STORAGE_MANIFEST)) {
            save(mAdapter.getCheckedAppsList());
        } else {
            PermissionsHelper.requestPermissions(this,
                    new String[]{PermissionsHelper.READ_PHONE_STATE_MANIFEST,
                            PermissionsHelper.WRITE_EXTERNAL_STORAGE_MANIFEST});
        }
    }

    private NewRequestAdapter mAdapter;
    private boolean mCheckAll = false;
    private boolean menuEnable;
    private Menu mMenu;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_app_select;
    }

    @Override
    protected void hookBeforeSetContentView() {
        super.hookBeforeSetContentView();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    @Override
    protected void init() {
        super.init();

        setToolbarCloseIndicator();

        setToolbarDoubleTapListener(new ToolbarOnGestureListener.DoubleTapListener() {
            @Override
            public void onDoubleTap() {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        menuEnable = false;

        Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {
            @Override
            public void call(Subscriber<? super List<AppInfo>> subscriber) {
                subscriber.onNext(AppInfoUtil.getComponentInfo(AppSelectActivity.this, true));
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AppInfo>>() {
                    @Override
                    public void call(List<AppInfo> list) {
                        setupRecyclerView(list);

                        dismissIndicator();
                        showRecyclerView();

                        menuEnable = true;
                        if (mMenu != null) {
                            onCreateOptionsMenu(mMenu);
                        }
                    }
                });

    }

    private void setupRecyclerView(List<AppInfo> appInfoList) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL,
                        false)
        );
        mAdapter = new NewRequestAdapter(mContext, appInfoList);
        mAdapter.setOnCheckListener(new NewRequestAdapter.OnCheckListener() {
            @Override
            public void OnEmpty() {
                showFab(false);
            }

            @Override
            public void OnUnEmpty() {
                showFab(true);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void dismissIndicator() {
        mIndicatorView.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showFab(boolean show) {
        mFAB.setShow(show);
        if (show) {
            mFAB.show();
        } else {
            mFAB.hide();
        }
    }

    @Override
    public void onBackPressed() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.content(getString(R.string.cancel_request));
        builder.onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    back();
                }
            }
        });
        builder.positiveText(getString(R.string.yes));
        builder.negativeText(getString(R.string.no));
        builder.show();
    }

    private void back() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (menuEnable) {
            getMenuInflater().inflate(R.menu.menu_app_select, menu);
        } else {
            mMenu = menu;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_just_show_without_custom) {
            item.setChecked(!item.isChecked());
            mAdapter.setShowAll(!item.isChecked());
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_premium_request) {

        } else if (id == R.id.action_select_all) {
            mCheckAll = !mCheckAll;
            mAdapter.checkAll(mCheckAll);
            if (mCheckAll) {
                showFab(true);
            } else {
                showFab(false);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void save(final List<AppInfo> list) {
        Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {
            @Override
            public void call(final Subscriber<? super List<AppInfo>> subscriber) {
                String deviceId = null;
                try {
                    TelephonyManager tm =
                            (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    deviceId = tm.getDeviceId();
                } catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace();
                    }
                }

                List<RequestBean> requestBeanList = new ArrayList<>();
                SharedPreferences sharedPreferences = getSharedPreferences("request package",
                        MODE_PRIVATE);

                for (int i = 0; i < list.size(); i++) {
                    RequestBean request = new RequestBean();
                    AppInfo app = list.get(i);
                    request.setAppPackage(app.getPackage());
                    request.setComponent(app.getCode());
                    request.setEnName(AppInfoUtil.getAppEnglishName(
                            AppSelectActivity.this, app.getPackage()));
                    request.setZhName(AppInfoUtil.getAppChineseName(
                            AppSelectActivity.this, app.getPackage()));
                    if (deviceId != null && sharedPreferences.getInt(app.getCode(), 0) == 0) {
                        request.setDeviceId(deviceId);
                        requestBeanList.add(request);
                        sharedPreferences.edit().putInt(app.getCode(), 1).apply();
                    }
                }
                RequestBean.saveAllInBackground(requestBeanList, new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        subscriber.onNext(null);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AppInfo>>() {
                    ProgressDialog mProgressDialog;

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (mProgressDialog == null) {
                            mProgressDialog = new ProgressDialog(AppSelectActivity.this);
                            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            mProgressDialog.setCanceledOnTouchOutside(false);
                        }
                        mProgressDialog.setMessage(getString(R.string.icon_request_sending));
                        mProgressDialog.show();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<AppInfo> list) {
                        mProgressDialog.dismiss();
                        Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                        AppSelectActivity.this.finish();
                    }
                });
    }
}
