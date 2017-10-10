package com.sorcerer.sorcery.iconpack;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sorcerer.sorcery.iconpack.test.TestActivity;

import rx_activity_result2.RxActivityResult;


/**
 * @description: *
 * @author: Sorcerer
 * *
 * @date: 2016/11/4
 */

public class Navigator {

    private Activity mActivity;

    Navigator(Activity activity) {
        mActivity = activity;
    }

    public static void toWebPage(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static Boolean toMail(Context context, String receiver, String subject, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{receiver});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    public static void toAppMarketSearchResult(Context context, String query) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://search?q=$query&c=apps"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/search?q=$query&c=apps"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    public void toSearch() {
        if (mActivity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mActivity;
            RxActivityResult.on(mActivity)
                    .startIntent(new Intent(mActivity, SearchActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            .putExtra("custom picker", mainActivity.isCustomPicker()))
                    .filter(activityResult -> activityResult.data() != null)
                    .subscribe(result -> mainActivity.onReturnCustomPickerRes(
                            result.data().getIntExtra("icon res", 0))
                    );
        } else {
            mActivity.startActivity(new Intent(mActivity, SearchActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }
    }

    void toIconRequest() {
        mainActivityShift(IconRequestActivity.class);
    }

    void toAppleActivity() {
        mainActivityShift(ApplyActivity.class);
    }

    void toDonateActivity() {
        mainActivityShift(DonateActivity.class);
    }

    void toHelpMarkdownActivity() {
        mainActivityShift(HelpActivity.class);
    }

    void toSettingsActivity() {
        mainActivityShift(SettingsActivity.class);
    }

    void toTestActivity() {
        Intent intent = new Intent(mActivity, TestActivity.class);
        mActivity.startActivity(intent);
    }

    private void mainActivityShift(Class<?> cls) {
        Intent intent = new Intent(mActivity, cls);
        mActivity.startActivity(intent);

        Boolean lessAnim = App.getInstance().prefs().lessAnim().get();
        if (lessAnim != null && lessAnim) {
            mActivity.overridePendingTransition(0, 0);
        } else {
            mActivity.overridePendingTransition(R.anim.slide_right_in, 0);
        }
    }
}
