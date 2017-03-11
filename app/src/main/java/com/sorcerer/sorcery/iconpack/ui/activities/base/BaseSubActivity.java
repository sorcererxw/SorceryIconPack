package com.sorcerer.sorcery.iconpack.ui.activities.base;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/7
 */

public abstract class BaseSubActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
//        new MaterializeBuilder(this)
//                .withTransparentStatusBar(false)
//                .withTintedStatusBar(true)
//                .withStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//                .withTranslucentStatusBarProgrammatically(true)
//                .build();

        mPrefs.lessAnim().asObservable().subscribe(lessAnim -> setSwipeEnabled(!lessAnim));
    }
}
