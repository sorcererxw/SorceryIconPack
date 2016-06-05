package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/5/28 0028.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext = this;
    protected Activity mActivity = this;

    protected final String TAG = getClass().getSimpleName();

    protected abstract int provideLayoutId();

    protected abstract void init();

    protected void hookBeforeSetContentView() {

    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        LayoutInflaterCompat
//                .setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        hookBeforeSetContentView();
        Log.i(TAG, "onCreate");
        setContentView(provideLayoutId());
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = SIP.getRefWatcher(this);
//        refWatcher.watch(this);
    }
}
