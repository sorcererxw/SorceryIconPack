package com.sorcerer.sorcery.iconpack.apply.appliers.database.pixel;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/9
 */

public class PixelApplySingleInstanceActivity extends PixelApplyActivity {
    public static void apply(Context context, boolean apply) {
        Intent intent = new Intent(context, PixelApplySingleInstanceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setAction(apply ? ACTION_APPLY : ACTION_RESTORE);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    @Override
    protected void doFinish() {
        new Handler().postDelayed(this::finish,5000);
    }
}
