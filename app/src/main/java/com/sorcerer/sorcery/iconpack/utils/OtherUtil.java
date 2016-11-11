package com.sorcerer.sorcery.iconpack.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/11
 */

public class OtherUtil {

    public static boolean showHead(Activity activity) {
        if (!ResourceUtil.getString(activity, R.string.language).equals("zh")) {
            return false;
        }
        return true;
    }
}
