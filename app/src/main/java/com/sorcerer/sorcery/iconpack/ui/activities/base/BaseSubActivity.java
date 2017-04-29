package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mikepenz.materialize.MaterializeBuilder;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/7
 */

public abstract class BaseSubActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

        mPrefs.lessAnim().asObservable().subscribe(lessAnim -> setSwipeEnabled(!lessAnim));

//        new MaterializeBuilder()
//                .withActivity(mActivity)
//                .withRootView(rootView())
//                .withStatusBarPadding(true)
//                .withTransparentStatusBar(true)
//                .withSystemUIHidden(false)
//                .withTranslucentNavigationBarProgrammatically(false)
//                .build();

//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
