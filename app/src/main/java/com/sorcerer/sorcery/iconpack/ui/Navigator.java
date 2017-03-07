package com.sorcerer.sorcery.iconpack.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.apply.ApplyActivity;
import com.sorcerer.sorcery.iconpack.customWorkshop.CustomWorkshopActivity;
import com.sorcerer.sorcery.iconpack.feedback.chat.FeedbackChatActivity;
import com.sorcerer.sorcery.iconpack.feedback.request.AppSelectActivity;
import com.sorcerer.sorcery.iconpack.help.HelpActivity;
import com.sorcerer.sorcery.iconpack.search.SearchActivity;
import com.sorcerer.sorcery.iconpack.settings.SettingsActivity;
import com.sorcerer.sorcery.iconpack.test.TestActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.DonateActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;

import rx_activity_result2.RxActivityResult;


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
                    .filter(activityResult -> activityResult.data() != null)
                    .subscribe(result -> {
                        mainActivity.onReturnCustomPickerRes(
                                result.data().getIntExtra("icon res", 0));
                    });
        } else {
            mActivity.startActivity(new Intent(mActivity, SearchActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }
    }

    public void toFeedbackChatActivity() {
        mActivity.startActivity(new Intent(mActivity, FeedbackChatActivity.class));
    }

    public void toIconRequest() {
        mActivity.startActivity(new Intent(mActivity, AppSelectActivity.class));
    }

    public void toAppleActivity() {
        mainActivityShift(ApplyActivity.class);
    }

    public void toDonateActivity() {
        mainActivityShift(DonateActivity.class);
    }

    public void toHelpActivity() {
        mainActivityShift(HelpActivity.class);
    }

    public void toSettingsActivity() {
        mainActivityShift(SettingsActivity.class);
    }

    public void toCustomWorkshopActivity() {
        mainActivityShift(CustomWorkshopActivity.class);
    }

    public void toTestActivity() {
        Intent intent = new Intent(mActivity, TestActivity.class);
        mActivity.startActivity(intent);
    }

    private void mainActivityShift(Class<?> cls) {
        Intent intent = new Intent(mActivity, cls);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_right_in, android.R.anim.fade_out);
    }

    public static void toWebPage(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
}
