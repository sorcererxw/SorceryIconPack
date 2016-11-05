package com.sorcerer.sorcery.iconpack.ui.exposedSearch;

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
public class FadeOutTransition extends AutoTransition {
    private FadeOutTransition() {

    }

    private static final int FADE_OUT_DURATION = 250;

    public static Transition withAction(TransitionListener finishingAction) {
        AutoTransition transition = new AutoTransition();
        transition.setDuration(FADE_OUT_DURATION);
        transition.addListener(finishingAction);
        return transition;
    }
}
