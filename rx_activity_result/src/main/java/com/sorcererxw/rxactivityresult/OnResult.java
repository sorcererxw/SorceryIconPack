package com.sorcererxw.rxactivityresult;

import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/15
 */

public interface OnResult {
    void response(int resultCode, @Nullable Intent data);
}
