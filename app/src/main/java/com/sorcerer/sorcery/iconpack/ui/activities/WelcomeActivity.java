package com.sorcerer.sorcery.iconpack.ui.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/21
 */

public class WelcomeActivity extends BaseActivity {
    @Override
    protected ViewGroup rootView() {
        return null;
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
