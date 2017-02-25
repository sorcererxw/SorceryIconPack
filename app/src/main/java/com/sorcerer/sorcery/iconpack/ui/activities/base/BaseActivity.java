package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVAnalytics;

import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/5/28 0028
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext = this;
    protected Activity mActivity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hookBeforeSetContentView();
        setContentView(provideLayoutId());
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int provideLayoutId();

    protected abstract void init();

    protected void hookBeforeSetContentView() {

    }
}
