package com.sorcerer.sorcery.iconpack.apply.database.smartisan;

import android.content.Context;
import android.content.Intent;

import com.sorcerer.sorcery.iconpack.apply.database.base.BaseApplyActivity;
import com.sorcerer.sorcery.iconpack.apply.database.base.ILauncherApplier;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/10
 */

public class SmartisanApplyProgressActivity extends BaseApplyActivity {
    protected static final String ACTION_APPLY = "action_apply";
    protected static final String ACTION_RESTORE = "action_restore";

    public static void apply(Context context, boolean apply) {
        Intent intent = new Intent(context, SmartisanApplyProgressActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(apply ? ACTION_APPLY : ACTION_RESTORE);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    @Override
    protected String packageName() {
        return "com.smartisanos.home";
    }

    @Override
    protected String componentName() {
        return "com.smartisanos.home.Launcher";
    }

    @Override
    protected ILauncherApplier applier() {
        return new SmartisanLauncherApplier(mActivity);
    }

}
