package com.sorcererxw.rxactivityresult;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.Nullable;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/15
 */

public class RxActivityResult {
    private static ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;

    public static void register(final Application application) {
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(application);
    }

    public static <T extends Activity> Builder<T> on(T activity) {
        return new Builder<>(activity);
    }

    public static class Builder<T extends Activity> {
        final Class mClass;
        final PublishSubject<Result<T>> mSubject = PublishSubject.create();

        public Builder(T activity) {
            if (mActivitiesLifecycleCallbacks == null) {
                throw new IllegalStateException("not registered");
            }

            mClass = activity.getClass();
        }

        public Observable<Result<T>> startIntent(Intent intent) {
            return startHolderActivity(new Request(intent));
        }

        private Observable<Result<T>> startHolderActivity(Request request) {
            request.setOnResult(mOnResultActivity);
            HolderActivity.setRequest(request);
            mActivitiesLifecycleCallbacks.getLiveActivity() // 获取当前应用顶层的Activity
                .subscribe(new Action1<Activity>() {
                    @Override
                    public void call(Activity activity) {
                        activity.startActivity(new Intent(activity, HolderActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                });

            return mSubject.asObservable();
        }

        private OnResult mOnResultActivity = new OnResult() {
            @Override
            public void response(int resultCode, @Nullable Intent data) {
                if (mActivitiesLifecycleCallbacks.getLiveActivityOrNull() == null
                        || mActivitiesLifecycleCallbacks.getLiveActivityOrNull()
                        .getClass() != mClass) {
                    return;
                }
                Activity activity = mActivitiesLifecycleCallbacks.getLiveActivityOrNull();
                mSubject.onNext(new Result<>((T) activity, resultCode, data));
                mSubject.onCompleted();
            }
        };
    }
}
