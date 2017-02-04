package com.sorcerer.sorcery.iconpack.ui.anim;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.AutoTransition;
import android.transition.Transition;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FadeInTransition extends AutoTransition {
    private FadeInTransition() {

    }

    private static final int FADE_IN_DURATION = 250;

    public static Transition createTransition() {
        AutoTransition transition = new AutoTransition();
        transition.setDuration(FADE_IN_DURATION);
        return transition;
    }
}
