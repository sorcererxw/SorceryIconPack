package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.net.avos.AvosClient;
import com.sorcerer.sorcery.iconpack.net.avos.AvosIconRequestBean;
import com.sorcerer.sorcery.iconpack.ui.activities.base.UniversalToolbarActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.RequestAdapter;
import com.sorcerer.sorcery.iconpack.ui.views.MyFloatingActionButton;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.Prefs.SorceryPrefs;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleSource;
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
//        new RxPermissions(this)
//                .request(Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe(grant -> {
//                    if (grant) {
//                        save(mAdapter.getCheckedAppsList());
//                    }
//                }, Timber::e);
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

        setToolbarDoubleTapListener(() -> mRecyclerView.smoothScrollToPosition(0));

        menuEnable = false;

        Observable.create(new ObservableOnSubscribe<List<AppInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppInfo>> e) throws Exception {
                e.onNext(PackageUtil.getComponentInfo(AppSelectActivity.this, true));
            }
        })
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
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.content(getString(R.string.cancel_request));
        builder.onAny((dialog, which) -> {
            if (which == DialogAction.POSITIVE) {
                back();
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
        String deviceId = SorceryPrefs.getInstance(AppSelectActivity.this).getUUID().getValue();
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
                                    List<AvosIconRequestBean> list)
                                    throws Exception {
                                return AvosClient.getInstance().postIconRequests(list);
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Boolean>>() {
                    ProgressDialog mProgressDialog;

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (mProgressDialog == null) {
                            mProgressDialog = new ProgressDialog(AppSelectActivity.this);
                            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            mProgressDialog.setCanceledOnTouchOutside(false);
                        }
                        mProgressDialog.setMessage(getString(R.string.icon_request_sending));
                        mProgressDialog.show();
                    }

                    @Override
                    public void onNext(List<Boolean> booleanList) {
                        Stream.of(booleanList)
                                .forEach(b -> Timber.d(String.valueOf(b)));
                    }


                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        mProgressDialog.dismiss();
                        Snackbar.make(mCoordinatorLayout, "Error", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        mProgressDialog.dismiss();
                        mAdapter.uncheckAfterSend();
                        showFab(false);
                        Toast.makeText(AppSelectActivity.this, "success", Toast.LENGTH_SHORT)
                                .show();
                        AppSelectActivity.this.finish();
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_in_scale, R.anim.activity_out_top_to_bottom);
    }
}
