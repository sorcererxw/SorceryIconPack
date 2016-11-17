package com.sorcerer.sorcery.iconpack.utils;

import android.app.Activity;
import android.content.Intent;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.AppSelectActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.ApplyActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.DonateActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.FeedbackActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.FeedbackChatActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.HelpActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.LabActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.SearchActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.SettingsActivity;
import com.sorcererxw.rxactivityresult.RxActivityResult;
import com.sorcererxw.rxactivityresult.Result;

import rx.functions.Action1;


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
        if (mActivity instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity) mActivity;
            RxActivityResult.on(mActivity)
                    .startIntent(new Intent(mActivity, SearchActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            .putExtra("custom picker", mainActivity.isCustomPicker()))
                    .subscribe(new Action1<Result>() {
                        @Override
                        public void call(Result result) {
                            if (result.getData() != null) {
                                mainActivity.onReturnCustomPickerRes(
                                        result.getData().getIntExtra("icon res", 0));
                            }
                        }
                    });
        } else {
            mActivity.startActivity(new Intent(mActivity, SearchActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }
    }

    public void toFeedbackChatActivity() {
        mActivity.startActivity(new Intent(mActivity, FeedbackChatActivity.class));
        mActivity.overridePendingTransition(
                R.anim.activity_in_bottom_to_top,
                R.anim.activity_out_scale);
    }

    public void toIconRequest() {
        mActivity.startActivity(new Intent(mActivity, AppSelectActivity.class));
        mActivity.overridePendingTransition(
                R.anim.activity_in_bottom_to_top,
                R.anim.activity_out_scale);
    }

    public void toLabActivity() {
        mainActivityShift(LabActivity.class);
    }

    public void toAppleActivity() {
        mainActivityShift(ApplyActivity.class);
    }

    public void toDonateActivity() {
        mainActivityShift(DonateActivity.class);
    }

    public void toFeedbackActivity() {
        mainActivityShift(FeedbackActivity.class);
    }

    public void toHelpActivity() {
        mainActivityShift(HelpActivity.class);
    }

    public void toSettingsActivity() {
        mainActivityShift(SettingsActivity.class);
    }

    private void mainActivityShift(Class<?> cls) {
        Intent intent = new Intent(mActivity, cls);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_right_in, android.R.anim.fade_out);
    }

}
