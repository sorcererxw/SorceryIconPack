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
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.net.leancloud.RequestBean;
import com.sorcerer.sorcery.iconpack.ui.activities.base.UniversalToolbarActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.RequestAdapter;
import com.sorcerer.sorcery.iconpack.ui.views.MyFloatingActionButton;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;
import com.sorcerer.sorcery.iconpack.util.ToolbarOnGestureListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
            new SaveRequestAsyncTask(this, mAdapter.getCheckedAppsList()).execute();
        } else {
            PermissionsHelper.requestPermissions(this,
                    new String[]{PermissionsHelper.READ_PHONE_STATE_MANIFEST,
                            PermissionsHelper.WRITE_EXTERNAL_STORAGE_MANIFEST});
        }
    }

    private RequestAdapter mAdapter;
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

        new LoadAppsAsyncTask(this, new LoadAppsAsyncTask.OnFinishCallback() {
            @Override
            public void onFinish(List<AppInfo> list) {
                setupRecyclerView(list);

                dismissIndicator();
                showRecyclerView();

                menuEnable = true;
                if (mMenu != null) {
                    onCreateOptionsMenu(mMenu);
                }
            }
        }).execute();

    }

    private static class LoadAppsAsyncTask extends AsyncTask<Void, Void, List<AppInfo>> {

        private Context mContext;
        private OnFinishCallback mCallback;

        public LoadAppsAsyncTask(Context context, OnFinishCallback callback) {
            mContext = context;
            mCallback = callback;
        }

        interface OnFinishCallback {
            void onFinish(List<AppInfo> list);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<AppInfo> doInBackground(Void... voids) {
            return AppInfoUtil.getComponentInfo(mContext, true);
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfoList) {
            super.onPostExecute(appInfoList);
            mCallback.onFinish(appInfoList);
        }
    }

    private void setupRecyclerView(List<AppInfo> appInfoList) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL,
                        false)
        );
        mAdapter = new RequestAdapter(mContext, appInfoList);
        mAdapter.setOnCheckListener(new RequestAdapter.OnCheckListener() {
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

    private class SaveRequestAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mProgressDialog;
        private Context mContext;
        private List<AppInfo> mAppInfoList;
        private String mDeviceId = null;

        public SaveRequestAsyncTask(Context context, List<AppInfo> appInfoList) {
            mContext = context;
            mAppInfoList = appInfoList;
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);

            try {
                TelephonyManager tm =
                        (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                mDeviceId = tm.getDeviceId();
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage(getString(R.string.icon_request_sending));
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<RequestBean> requestBeanList = new ArrayList<>();
            SharedPreferences sharedPreferences = getSharedPreferences("request package",
                    MODE_PRIVATE);

            for (int i = 0; i < mAppInfoList.size(); i++) {
                RequestBean request = new RequestBean();
                AppInfo app = mAppInfoList.get(i);
                request.setAppPackage(app.getPackage());
                request.setComponent(app.getCode());
                request.setEnName(AppInfoUtil.getAppEnglishName(
                        AppSelectActivity.this, app.getPackage()));
                request.setZhName(AppInfoUtil.getAppChineseName(
                        AppSelectActivity.this, app.getPackage()));
                if (mDeviceId != null && sharedPreferences.getInt(app.getCode(), 0) == 0) {
                    request.setDeviceId(mDeviceId);
                    requestBeanList.add(request);
                    sharedPreferences.edit().putInt(app.getCode(), 1).apply();
                }
            }
            RequestBean.saveAllInBackground(requestBeanList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
            AppSelectActivity.this.finish();
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
}
