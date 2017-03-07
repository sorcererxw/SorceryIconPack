package com.sorcerer.sorcery.iconpack;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/7
 */

@Module
class AppModule {
    private final App mApp;

    AppModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return mApp;
    }

    @Provides
    @Singleton
    SorceryPrefs provideSorceryPrefs() {
        return new SorceryPrefs(mApp);
    }
}
