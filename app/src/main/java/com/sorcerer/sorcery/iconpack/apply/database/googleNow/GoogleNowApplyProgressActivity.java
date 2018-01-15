package com.sorcerer.sorcery.iconpack.apply.database.googleNow;

import android.content.Context;
import android.content.Intent;

import com.sorcerer.sorcery.iconpack.apply.database.base.BaseApplyActivity;
import com.sorcerer.sorcery.iconpack.apply.database.base.ILauncherApplier;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/11
 */

public class GoogleNowApplyProgressActivity extends BaseApplyActivity {

    public static void apply(Context context, boolean apply) {
        Intent intent = new Intent(context, GoogleNowApplyProgressActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(apply ? ACTION_APPLY : ACTION_RESTORE);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    @Override
    protected String packageName() {
        return "com.google.android.googlequicksearchbox";
    }

    @Override
    protected String componentName() {
        return "com.google.android.launcher.GEL";
    }

    @Override
    protected ILauncherApplier applier() {
        return new GoogleNowLauncherApplier(mActivity, mPrefs.isGoogleNowDisableWork().getValue());
    }
}
