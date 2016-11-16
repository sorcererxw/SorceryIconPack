package com.sorcererxw.rxactivityresult;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/15
 */

public class ActivitiesLifecycleCallbacks {
    final Application mApplication;
    volatile Activity mLiveActivityOrNull;
    Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    public ActivitiesLifecycleCallbacks(Application application) {
        mApplication = application;
        registerActivityLiftcycle();
    }

    private void registerActivityLiftcycle() {
        if (mActivityLifecycleCallbacks != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
        mActivityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                mLiveActivityOrNull = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mLiveActivityOrNull = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                mLiveActivityOrNull = null;
            }
        };

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    Activity getLiveActivityOrNull() {
        return mLiveActivityOrNull;
    }

    volatile boolean mEmitted = false;

    Observable<Activity> getLiveActivity() {
        mEmitted = false;
        return Observable.interval(50, 50, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Activity>() {
                    @Override
                    public Activity call(Long aLong) {
                        return mLiveActivityOrNull;
                    }
                })
                .takeWhile(new Func1<Activity, Boolean>() {
                    @Override
                    public Boolean call(Activity activity) {
                        boolean continueEmitting = !mEmitted;
                        mEmitted = activity != null;
                        return continueEmitting;
                    }
                })
                .filter(new Func1<Activity, Boolean>() {
                    @Override
                    public Boolean call(Activity activity) {
                        return activity != null;
                    }
                });
    }
}
