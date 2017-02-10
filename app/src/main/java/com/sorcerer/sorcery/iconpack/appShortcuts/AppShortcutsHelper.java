package com.sorcerer.sorcery.iconpack.appShortcuts;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.Collections;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/9
 */

@TargetApi(25)
public class AppShortcutsHelper {
    public static final boolean ENABLE_SHORTCUT = Build.VERSION.SDK_INT >= 25;
    ;
    private Context mContext;

    private ShortcutManager mShortcutManager;

    public AppShortcutsHelper(Context context) {
        mContext = context;
        mShortcutManager = context.getSystemService(ShortcutManager.class);
    }

    private static final String APPLY_PIXEL_ID = "apply_pixel";

    public void addApplyPixelShortcut() {
        if (!ENABLE_SHORTCUT) {
            return;
        }
        ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(mContext, APPLY_PIXEL_ID)
                .setLongLabel(
                        ResourceUtil.getString(mContext, R.string.shortcuts_apply_pixel_long_label))
                .setShortLabel(ResourceUtil
                        .getString(mContext, R.string.shortcuts_apply_pixel_short_label))
                .setIcon(Icon.createWithResource(mContext,
                        R.drawable.ic_shortcut_color_lens))
                .setIntent(new Intent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        .setAction(AppShortcutsReceiverActivity.ACTION_APPLY_PIXEL)
                        .setClass(mContext, AppShortcutsReceiverActivity.class))
                .build();

        mShortcutManager.addDynamicShortcuts(Collections.singletonList(shortcutInfo));
    }

    public void removeApplyPixelShortcut() {
        if (!ENABLE_SHORTCUT) {
            return;
        }
        mShortcutManager.removeDynamicShortcuts(Collections.singletonList(APPLY_PIXEL_ID));
    }
}
