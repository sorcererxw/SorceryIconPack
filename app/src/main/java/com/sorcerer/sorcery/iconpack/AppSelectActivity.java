package com.sorcerer.sorcery.iconpack;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.data.db.RequestDbManager;
import com.sorcerer.sorcery.iconpack.data.models.AppInfo;
import com.sorcerer.sorcery.iconpack.network.avos.AvosClient;
import com.sorcerer.sorcery.iconpack.network.avos.models.AvosIconRequestBean;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.ui.views.MyFloatingActionButton;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AppSelectActivity extends BaseSubActivity {

    @BindView(R.id.coordinatorLayout_request)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.recyclerView_app_select)
    RecyclerView mRecyclerView;

    @BindView(R.id.avLoadingIndicatorView_icon_select)
    AVLoadingIndicatorView mIndicatorView;

    @BindView(R.id.fab_app_select)
    MyFloatingActionButton mFAB;
    private RequestAdapter mAdapter;
    private boolean mCheckAll = false;
    private boolean menuEnable;
    private Menu mMenu;
    private RequestDbManager mRequestDbManager;

    @OnClick(R.id.fab_app_select)
    void onFABClick() {
        save(mAdapter.getCheckedAppsList());
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_app_select;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarBackIndicator();

        setToolbarDoubleTapListener(() -> mRecyclerView.smoothScrollToPosition(0));

        menuEnable = false;

        mRequestDbManager = mDb.requestDbManager();

        Observable.create(
                (ObservableOnSubscribe<List<AppInfo>>) e -> e
                        .onNext(PackageUtil.getComponentInfo(
                                AppSelectActivity.this, true)))
                .doOnNext(appInfos -> mRequestDbManager
                        .saveComponent(Stream.of(appInfos).map(AppInfo::getCode)
                                .collect(Collectors.toList())))
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
        mAdapter = new RequestAdapter(mContext, appInfoList, mRequestDbManager);
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
        String deviceId = mPrefs.getUuid().getValue();
        SharedPreferences sharedPreferences = getSharedPreferences("request package", MODE_PRIVATE);
        Observable.fromIterable(list)
                .filter(appInfo -> deviceId != null
                        && deviceId.length() > 0
                        && (sharedPreferences.getInt(appInfo.getCode(), 0) == 0
                        || BuildConfig.DEBUG))
                .map(appInfo -> {
                    sharedPreferences.edit().putInt(appInfo.getCode(), 1).apply();
                    return new AvosIconRequestBean(
                            PackageUtil.getAppChineseName(
                                    AppSelectActivity.this,
                                    appInfo.getPackage()),
                            PackageUtil.getAppEnglishName(
                                    AppSelectActivity.this,
                                    appInfo.getPackage()),
                            appInfo.getPackage(),
                            appInfo.getCode(),
                            deviceId
                    );
                })
                .toList()
                .toObservable()
                .flatMap(list1 -> {
                    mRequestDbManager.setRequested(Stream.of(list1).map(
                            AvosIconRequestBean::getComponent)
                            .collect(Collectors.toList()));
                    return AvosClient.getInstance().postIconRequests(list1);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Boolean>>() {
                    private Dialog mProgressDialog;

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (mProgressDialog == null) {
                            mProgressDialog = Dialogs.INSTANCE.indeterminateProgressDialog(mContext,
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
                        Snackbar.make(mCoordinatorLayout, R.string.icon_request_send_success,
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    static class RequestAdapter extends
            RecyclerView.Adapter<RequestAdapter.AppItemViewHolder> {

        private Context mContext;
        private List<CheckAppInfo>
                mAppInfoList;
        private boolean mShowAll = false;
        private RequestAdapter.OnCheckListener
                mOnCheckListener;
        private RequestDbManager mRequestDbManager;

        RequestAdapter(Context context, List<AppInfo> appInfoList,
                       RequestDbManager requestDbManager) {
            mContext = context;
            mRequestDbManager = requestDbManager;
            mAppInfoList = new ArrayList<>();
            mAppInfoList.addAll(Stream.range(0, appInfoList.size())
                    .map(i -> new RequestAdapter.CheckAppInfo(
                            appInfoList.get(i), false))
                    .collect(Collectors.toList()));
        }

        @Override
        public RequestAdapter.AppItemViewHolder onCreateViewHolder(
                ViewGroup parent, int viewType) {
            return new RequestAdapter.AppItemViewHolder(
                    LayoutInflater.from(mContext)
                            .inflate(R.layout.item_icons_request, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(
                final RequestAdapter.AppItemViewHolder holder,
                int position) {
            final int realPos = getItem(position);

            holder.itemView
                    .setOnClickListener(v -> holder.check.setChecked(!holder.check.isChecked()));
            holder.check.setOnCheckedChangeListener(null);
            holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mAppInfoList.get(realPos).setChecked(isChecked);
                if (isChecked) {
                    if (getCheckedCount() >= 1 && mOnCheckListener != null) {
                        mOnCheckListener.OnUnEmpty();
                    }
                } else {
                    if (getCheckedCount() == 0 && mOnCheckListener != null) {
                        mOnCheckListener.OnEmpty();
                    }
                }
            });
            holder.label.setText(mAppInfoList.get(realPos).getName());
            holder.icon.setImageDrawable(mAppInfoList.get(realPos).getIcon());

            holder.check.setChecked(mAppInfoList.get(realPos).isChecked());

            mRequestDbManager.isRequest(mAppInfoList.get(realPos).getCode())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(requested -> {
                        if (requested) {
                            holder.setEnable(false);

                            holder.times.setText(R.string.icon_request_requested);
                        } else {
                            holder.setEnable(true);

                            if (mAppInfoList.get(realPos).getRequestedTimes() == -1) {
                                holder.setTimes(-1);
                                AvosClient.getInstance()
                                        .getAppRequestedTime(mAppInfoList.get(realPos).getPackage())
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(times -> {
                                            mAppInfoList.get(realPos).setRequestedTimes(times);
                                            holder.setTimes(times);
                                        }, Timber::e);
                            } else {
                                holder.setTimes(mAppInfoList.get(realPos).getRequestedTimes());
                            }
                        }
                    });
        }

        @Override
        public int getItemCount() {
            if (mShowAll) {
                return mAppInfoList.size();
            } else {
                return (int) Stream.of(mAppInfoList)
                        .filter(value -> !value.isHasCustomIcon())
                        .count();
            }
        }

        private int getCheckedCount() {
            return (int) Stream.of(mAppInfoList)
                    .filter(RequestAdapter.CheckAppInfo::isChecked)
                    // (hasCustomIcon && showAll) || !hasCustomIcon
                    .filter(value -> !value.isHasCustomIcon() || mShowAll)
                    .count();
        }

        private int getItem(int pos) {
            if (mShowAll) {
                return pos;
            } else {
                int tmp = 0;
                for (int i = 0; i < mAppInfoList.size(); i++) {
                    if (!mAppInfoList.get(i).isHasCustomIcon()) {
                        if (tmp == pos) {
                            return i;
                        } else {
                            tmp++;
                        }
                    }
                }
            }
            return pos;
        }

        void setOnCheckListener(RequestAdapter.OnCheckListener onCheckListener) {
            mOnCheckListener = onCheckListener;
        }

        List<AppInfo> getCheckedAppsList() {
            return Stream.of(mAppInfoList).filter(
                    RequestAdapter.CheckAppInfo::isChecked)
                    .collect(Collectors.toList());
        }

        void uncheckAfterSend() {
            Stream.of(mAppInfoList).filter(
                    RequestAdapter.CheckAppInfo::isChecked)
                    .forEach(cai -> {
                        cai.setChecked(false);
                        cai.setRequestedTimes(-1);
                    });
            notifyDataSetChanged();
        }

        void checkAll(boolean check) {
            if (check) {
                Stream.of(mAppInfoList).forEach(cai -> {
                    if (mShowAll) {
                        cai.setChecked(true);
                    } else if (!cai.isHasCustomIcon()) {
                        cai.setChecked(true);
                    }
                });
            } else {
                Stream.of(mAppInfoList).forEach(cai -> cai.setChecked(false));
            }
            notifyDataSetChanged();
        }

        void setShowAll(boolean isShowAll) {
            mShowAll = isShowAll;
            List<CheckAppInfo> tmp =
                    new ArrayList<>(mAppInfoList);

            mAppInfoList.clear();
            mAppInfoList.addAll(tmp);
            notifyDataSetChanged();
            if (!isShowAll) {
                Stream.of(mAppInfoList).filter(cai -> cai.isChecked() && cai.isHasCustomIcon())
                        .forEach(cai -> cai.setChecked(false));
                if (getCheckedCount() == 0) {
                    mOnCheckListener.OnEmpty();
                }
            }
        }

        interface OnCheckListener {
            void OnEmpty();

            void OnUnEmpty();
        }

        private static class CheckAppInfo extends AppInfo {

            private boolean mChecked = false;

            CheckAppInfo(AppInfo appInfo, Boolean check) {
                setRequestedTimes(appInfo.getRequestedTimes());
                setCode(appInfo.getCode());
                setHasCustomIcon(appInfo.isHasCustomIcon());
                setRequestedTimes(appInfo.getRequestedTimes());
                setName(appInfo.getName());
                setIcon(appInfo.getIcon());

                mChecked = check;
            }

            boolean isChecked() {
                return mChecked;
            }

            void setChecked(boolean checked) {
                mChecked = checked;
            }
        }

        static class AppItemViewHolder extends RecyclerView.ViewHolder {

            private static final int[] TIMES_POINT = {
                    1, 10, 50, 100, 200, 500, 1000, 2000, 5000, 10000
            };
            private static String mPrefixTimes;
            private static String mSuffixTimes;
            @BindView(R.id.imageVIew_icon_request_icon)
            ImageView icon;
            @BindView(R.id.textView_icon_request_label)
            TextView label;
            @BindView(R.id.textView_icon_request_times)
            TextView times;
            @BindView(R.id.checkBox_icon_request_check)
            CheckBox check;

            AppItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                String t =
                        ResourceUtil.getString(itemView.getContext(), R.string.icon_request_times);
                mPrefixTimes = t.split("#")[0];
                mSuffixTimes = t.split("#")[1];
            }

            private static String getCountString(int count) {
                if (count == 0) {
                    return "0";
                }
                for (int i = 0; i < TIMES_POINT.length - 1; i++) {
                    if (count >= TIMES_POINT[i] && count < TIMES_POINT[i + 1]) {
                        return TIMES_POINT[i] + "~" + TIMES_POINT[i + 1];
                    }
                }
                return TIMES_POINT[TIMES_POINT.length - 1] + "+";
            }

            void setTimes(int count) {
                if (count >= 0) {
                    String t = mPrefixTimes + getCountString(count) + mSuffixTimes;
                    times.setText(t);
                } else {
                    times.setText("......");
                }
            }

            void setEnable(boolean enable) {
                if (enable) {
                    itemView.setEnabled(true);
                    check.setEnabled(true);
                } else {
                    itemView.setEnabled(false);
                    check.setChecked(false);
                    check.setEnabled(false);
                }
            }
        }
    }
}
