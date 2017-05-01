package com.sorcerer.sorcery.iconpack;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sorcerer.sorcery.iconpack.apply.ApplyActivity;
import com.sorcerer.sorcery.iconpack.customWorkshop.CustomWorkshopActivity;
import com.sorcerer.sorcery.iconpack.feedback.request.AppSelectActivity;
import com.sorcerer.sorcery.iconpack.search.SearchActivity;
import com.sorcerer.sorcery.iconpack.settings.SettingsActivity;
import com.sorcerer.sorcery.iconpack.test.TestActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.DonateActivity;

import javax.inject.Inject;

import rx_activity_result2.RxActivityResult;


/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

@SuppressWarnings("WeakerAccess")
public class Navigator {
    private Activity mActivity;

    @Inject
    SorceryPrefs mPrefs;

    public Navigator(Activity activity) {
        mActivity = activity;
        App.getInstance().getAppComponent().inject(this);
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

    public void toIconRequest() {
        mainActivityShift(AppSelectActivity.class);
    }

    public void toAppleActivity() {
        mainActivityShift(ApplyActivity.class);
    }

    public void toDonateActivity() {
        mainActivityShift(DonateActivity.class);
    }

    public void toHelpMarkdownActivity() {
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

        Boolean lessAnim = mPrefs.lessAnim().get();
        if (lessAnim != null && lessAnim) {
            mActivity.overridePendingTransition(0, 0);
        } else {
            mActivity.overridePendingTransition(R.anim.slide_right_in, 0);
        }
    }

    public static void toWebPage(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static boolean toMail(Context context, String receiver, String subject, String content) {
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
                    Uri.parse("market://search?q=" + query + "&c=apps"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/search?q=" + query + "&c=apps"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
