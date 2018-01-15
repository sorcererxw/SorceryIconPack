package com.sorcerer.sorcery.iconpack.apply.xposed;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.mikepenz.materialize.util.UIUtils;
import com.sorcerer.sorcery.iconpack.ApplyActivity;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.utils.PackageUtil;
import com.sorcerer.sorcery.iconpack.utils.RxSU;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.sorcerer.sorcery.iconpack.apply.xposed.Constants.XPOSED_PREFERENCE;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/24
 */

public class XposedApplyFragment extends PreferenceFragmentCompat {

    private LinearLayout mSwitchLayout;
    private Switch mActiveSwitch;
    private RxSharedPreferences mSharedPreferences;
    private Preference<Boolean> mActivePreference;
    //preference_category_options
    private PreferenceCategory mOptionPreferenceCategory;
    //preference_refresh
    private android.support.v7.preference.Preference mRefreshPreference;
    //preference_clear_nova_cache
    private android.support.v7.preference.Preference mClearNovaCachePreference;
    // preference_reboot
    private android.support.v7.preference.Preference mRebootPreference;
    // preference_selinux
    private android.support.v7.preference.Preference mSELinuxPreference;

    public static XposedApplyFragment newInstance() {
        return new XposedApplyFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_apply_xposed, rootKey);

        mOptionPreferenceCategory =
                (PreferenceCategory) findPreference("preference_category_options");
        mRefreshPreference = findPreference("preference_refresh");
        mClearNovaCachePreference = findPreference("preference_clear_nova_cache");
        mRebootPreference = findPreference("preference_reboot");
        mSELinuxPreference = findPreference("preference_selinux");
    }

    @Override
    public void onStart() {
        super.onStart();
        getApplyActivity().setTitle(R.string.nav_item_apply_xposed);

        if (mSwitchLayout == null || mActiveSwitch == null) {
            mSwitchLayout = new LinearLayout(getContext());
            Toolbar.LayoutParams layoutParams =
                    new Toolbar.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
            layoutParams.gravity = Gravity.END;
            mSwitchLayout.setLayoutParams(layoutParams);
            mSwitchLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            mSwitchLayout.setOrientation(LinearLayout.VERTICAL);
            int padding = (int) UIUtils.convertDpToPixel(16, getContext());
            mSwitchLayout.setPadding(
                    padding,
                    0,
                    padding,
                    0
            );

            mActiveSwitch = new Switch(getContext());
            mSwitchLayout.addView(mActiveSwitch);
        }
        getApplyActivity().getToolbar().addView(mSwitchLayout);

        mActiveSwitch.postDelayed(this::init, 100);
    }

    private void init() {
        mRefreshPreference.setOnPreferenceClickListener(preference -> {
            XposedInstaller.refresh(getContext())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<IconReplacement>>() {
                        private Dialog mProgressDialog;

                        @Override
                        public void onSubscribe(Disposable d) {
                            mProgressDialog =
                                    Dialogs.indeterminateProgressDialog(getActivity(),
                                            "waiting");
                            mProgressDialog.show();
                        }

                        @Override
                        public void onNext(List<IconReplacement> iconReplacements) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e);
                            Toast.makeText(getContext(), "fail: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                        @Override
                        public void onComplete() {
                            mProgressDialog.dismiss();
                            Toast.makeText(getContext(),
                                    R.string.apply_by_xposed_refresh_success_toast_content,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            return true;
        });

        mClearNovaCachePreference.setOnPreferenceClickListener(preference -> {
            RxSU.getInstance().su()
                    .filter(grant -> grant)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                        @Override
                        public ObservableSource<Boolean> apply(Boolean aBoolean)
                                throws Exception {
                            return new RxPermissions(getActivity())
                                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .filter(grant -> grant)
                    .observeOn(Schedulers.newThread())
                    .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                        @Override
                        public ObservableSource<List<String>> apply(Boolean aBoolean)
                                throws Exception {
                            return Utils.clearNovaCache();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        private Dialog mProgressDialog;

                        @Override
                        public void onSubscribe(Disposable d) {
                            mProgressDialog =
                                    Dialogs
                                            .indeterminateProgressDialog(getActivity(),
                                                    "waiting");
                            mProgressDialog.show();
                        }

                        @Override
                        public void onNext(List<String> list) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e);
                            Toast.makeText(getContext(), "fail: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                        @Override
                        public void onComplete() {
                            mProgressDialog.dismiss();
                        }
                    });
            return true;
        });

        if (PackageUtil.isPackageInstalled(getContext(), "com.teslacoilsw.launcher")) {
            if (mOptionPreferenceCategory.findPreference("preference_clear_nova_cache")
                    == null) {
                mOptionPreferenceCategory.addPreference(mClearNovaCachePreference);
            }
        } else {
            if (mOptionPreferenceCategory.findPreference("preference_clear_nova_cache")
                    != null) {
                mOptionPreferenceCategory.removePreference(mClearNovaCachePreference);
            }
        }

        mRebootPreference.setOnPreferenceClickListener(preference -> {
            RxSU.getInstance().su().filter(grant -> grant)
                    .flatMap(new Function<Boolean, ObservableSource<List<String>>>() {
                        @Override
                        public ObservableSource<List<String>> apply(Boolean aBoolean)
                                throws Exception {
                            return Utils.reboot();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<String> list) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d(e);
                            Toast.makeText(getContext(), "fail: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            return true;
        });

        Utils.isSeLinuxPermissive()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(permissive -> {
                    if (permissive) {
                        if (mOptionPreferenceCategory.findPreference("preference_selinux")
                                != null) {
                            mOptionPreferenceCategory.removePreference(mSELinuxPreference);
                        }
                    } else {
                        mSELinuxPreference
                                .setTitle(R.string.preference_apply_xposed_selinux_executing);
                        if (mOptionPreferenceCategory.findPreference("preference_selinux")
                                == null) {
                            mOptionPreferenceCategory.addPreference(mSELinuxPreference);
                        }
                    }
                }, Timber::e);

        mSharedPreferences = RxSharedPreferences.create(
                getActivity().getSharedPreferences(XPOSED_PREFERENCE, Context.MODE_PRIVATE)
        );

        mActivePreference = mSharedPreferences.getBoolean(Constants.ACTIVE_PREFERENCE_KEY);

        if (mActivePreference.isSet() && mActivePreference.get()) {
            mActiveSwitch.setChecked(true);
        }

        mActiveSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                XposedInstaller.install(getActivity())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<IconReplacement>>() {
                            private Dialog mProgressDialog;

                            @Override
                            public void onSubscribe(Disposable d) {
                                mProgressDialog =
                                        Dialogs.indeterminateProgressDialog(getActivity(),
                                                "waiting");
                                mProgressDialog.show();
                            }

                            @Override
                            public void onNext(List<IconReplacement> iconReplacements) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e);
                                Toast.makeText(getContext(), "fail: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                                mActivePreference.set(false);
                                mActiveSwitch.setChecked(false);
                            }

                            @Override
                            public void onComplete() {
                                mProgressDialog.dismiss();
                                mActivePreference.set(true);
                                mActiveSwitch.setChecked(true);

                                Dialogs.builder(getContext())
                                        .title(R.string.apply_by_xposed_success_dialog_title)
                                        .content(R.string.apply_by_xposed_success_dialog_content)
                                        .negativeText(
                                                R.string.apply_by_xposed_success_dialog_negative)
                                        .positiveText(
                                                R.string.apply_by_xposed_success_dialog_positive)
                                        .autoDismiss(true)
                                        .onPositive((dialog, which) -> {
                                            RxSU.getInstance().su().filter(grant -> grant)
                                                    .flatMap(
                                                            new Function<Boolean, ObservableSource<List<String>>>() {
                                                                @Override
                                                                public ObservableSource<List<String>> apply(
                                                                        Boolean aBoolean)
                                                                        throws Exception {
                                                                    return Utils.reboot();
                                                                }
                                                            })
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Observer<List<String>>() {
                                                        @Override
                                                        public void onSubscribe(Disposable d) {

                                                        }

                                                        @Override
                                                        public void onNext(List<String> list) {

                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            Timber.d(e);
                                                            Toast.makeText(getContext(),
                                                                    "fail: " + e.getMessage(),
                                                                    Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onComplete() {

                                                        }
                                                    });
                                        })
                                        .build().show();
                            }
                        });
            } else {
                mActivePreference.set(false);
                mActiveSwitch.setChecked(false);
            }
        });

        mActivePreference.asObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(active -> {
                    mOptionPreferenceCategory.setEnabled(active);
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        getApplyActivity().getToolbar().removeView(mSwitchLayout);
    }

    private ApplyActivity getApplyActivity() {
        return (ApplyActivity) getActivity();
    }
}
