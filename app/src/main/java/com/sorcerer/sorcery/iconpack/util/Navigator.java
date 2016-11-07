package com.sorcerer.sorcery.iconpack.util;

import android.app.Activity;
import android.content.Intent;

import com.sorcerer.sorcery.iconpack.ui.activities.AppSelectActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.SearchActivity;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

public class Navigator {
    private Activity mActivity;

    public Navigator(Activity activity) {
        mActivity = activity;
    }

    public void toSearch() {
        mActivity.startActivity(new Intent(mActivity, SearchActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void toIconRequest() {
        mActivity.startActivity(new Intent(mActivity, AppSelectActivity.class));
    }
}
