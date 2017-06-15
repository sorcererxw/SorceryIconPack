package com.sorcerer.sorcery.iconpack.shortcuts

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import com.sorcerer.sorcery.iconpack.R
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/2/9
 */

@TargetApi(25)
class AppShortcutsHelper(private val mContext: Context) {

    private val mShortcutManager: ShortcutManager = mContext.getSystemService(
            ShortcutManager::class.java)

    fun addApplyPixelShortcut() {
        if (!ENABLE_SHORTCUT) {
            return
        }
        val shortcutInfo = ShortcutInfo.Builder(mContext, APPLY_PIXEL_ID)
                .setLongLabel(
                        ResourceUtil.getString(mContext, R.string.shortcuts_apply_pixel_long_label))
                .setShortLabel(ResourceUtil
                        .getString(mContext, R.string.shortcuts_apply_pixel_short_label))
                .setIcon(Icon.createWithResource(mContext,
                        R.drawable.ic_shortcut_color_lens))
                .setIntent(Intent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        .setAction(AppShortcutsReceiverActivity.ACTION_APPLY_PIXEL)
                        .setClass(mContext, AppShortcutsReceiverActivity::class.java))
                .build()

        mShortcutManager.addDynamicShortcuts(listOf(shortcutInfo))
    }

    fun removeApplyPixelShortcut() {
        if (!ENABLE_SHORTCUT) {
            return
        }
        mShortcutManager.removeDynamicShortcuts(listOf(APPLY_PIXEL_ID))
    }

    companion object {
        val ENABLE_SHORTCUT = Build.VERSION.SDK_INT >= 25

        private val APPLY_PIXEL_ID = "apply_pixel"
    }
}
