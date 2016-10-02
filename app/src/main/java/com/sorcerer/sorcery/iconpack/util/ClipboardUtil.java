package com.sorcerer.sorcery.iconpack.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/2
 */

public class ClipboardUtil {
    public static void clip(Context context, String label, String content) {
        ClipboardManager clipboard =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip =
                ClipData.newPlainText(label, content);
        clipboard.setPrimaryClip(clip);
    }
}
