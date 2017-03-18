package com.sorcerer.sorcery.iconpack.ui.activities.base;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
    }
}
