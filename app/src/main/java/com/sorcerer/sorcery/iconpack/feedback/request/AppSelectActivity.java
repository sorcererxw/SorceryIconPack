package com.sorcerer.sorcery.iconpack.feedback.request;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.AppInfo;
import com.sorcerer.sorcery.iconpack.network.avos.AvosClient;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosIconRequestBean;
import com.sorcerer.sorcery.iconpack.ui.activities.base.UniversalToolbarActivity;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.ui.views.MyFloatingActionButton;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AppSelectActivity extends UniversalToolbarActivity {

    @BindView(R.id.coordinatorLayout_app_select)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.recyclerView_app_select)
    RecyclerView mRecyclerView;

    @BindView(R.id.avLoadingIndicatorView_icon_select)
    AVLoadingIndicatorView mIndicatorView;

    @BindView(R.id.fab_app_select)
    MyFloatingActionButton mFAB;

    @OnClick(R.id.fab_app_select)
    void onFABClick() {
        save(mAdapter.getCheckedAppsList());
    }

    private RequestAdapter mAdapter;
    private boolean mCheckAll = false;
    private boolean menuEnable;
    private Menu mMenu;

    @Override
    protected ViewGroup rootView() {
        return mCoordinatorLayout;
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_app_select;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        setToolbarCloseIndicator();

        setToolbarDoubleTapListener(() -> mRecyclerView.smoothScrollToPosition(0));

        menuEnable = false;

        Observable.create((ObservableOnSubscribe<List<AppInfo>>)
                e -> e.onNext(PackageUtil.getComponentInfo(AppSelectActivity.this, true)))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    setupRecyclerView(list);

                    dismissIndicator();
                    showRecyclerView();

                    menuEnable = true;
                    if (mMenu != null) {
                        onCreateOptionsMenu(mMenu);
                    }
                }, Timber::e);
    }

    private void setupRecyclerView(List<AppInfo> appInfoList) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
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
        if (show) {
            mFAB.show();
        } else {
            mFAB.hide();
        }
    }

    @Override
    public void onBackPressed() {
        MaterialDialog.Builder builder = Dialogs.builder(this);
        builder.content(getString(R.string.cancel_request));
        builder.onPositive((dialog, which) -> {
            super.onBackPressed();
        });
        builder.positiveText(getString(R.string.yes));
        builder.negativeText(getString(R.string.no));
        builder.show();
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
        String deviceId = mPrefs.getUUID().getValue();
        SharedPreferences sharedPreferences = getSharedPreferences("request package", MODE_PRIVATE);
        Observable.fromIterable(list)
                .filter(appInfo -> deviceId != null
                        && deviceId.length() > 0
                        && (sharedPreferences.getInt(appInfo.getCode(), 0) == 0
                        || BuildConfig.DEBUG))
                .map(appInfo -> {
                    sharedPreferences.edit().putInt(appInfo.getCode(), 1).apply();
                    return new AvosIconRequestBean(
                            PackageUtil.getAppChineseName(AppSelectActivity.this,
                                    appInfo.getPackage()),
                            PackageUtil.getAppEnglishName(AppSelectActivity.this,
                                    appInfo.getPackage()),
                            appInfo.getPackage(),
                            appInfo.getCode(),
                            deviceId
                    );
                })
                .toList()
                .toObservable()
                .flatMap(
                        new Function<List<AvosIconRequestBean>, ObservableSource<List<Boolean>>>() {
                            @Override
                            public ObservableSource<List<Boolean>> apply(
                                    List<AvosIconRequestBean> list) throws Exception {
                                return AvosClient.getInstance().postIconRequests(list);
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Boolean>>() {
                    private Dialog mProgressDialog;

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (mProgressDialog == null) {
                            mProgressDialog = Dialogs.indeterminateProgressDialog(mContext,
                                    getString(R.string.icon_request_sending));
                            mProgressDialog.setCancelable(false);
                        }
                        mProgressDialog.show();
                    }

                    @Override
                    public void onNext(List<Boolean> booleanList) {
                        Stream.of(booleanList).forEach(b -> Timber.d(String.valueOf(b)));
                    }


                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        mProgressDialog.dismiss();
                        Snackbar.make(mCoordinatorLayout, R.string.error, Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onComplete() {
                        mProgressDialog.dismiss();
                        mAdapter.uncheckAfterSend();
                        showFab(false);
                        Toast.makeText(AppSelectActivity.this, R.string.icon_request_send_success,
                                Toast.LENGTH_SHORT)
                                .show();
                        AppSelectActivity.this.finish();
                    }
                });
    }
}
