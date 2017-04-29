package com.sorcerer.sorcery.iconpack;

import android.content.Context;

import com.sorcerer.sorcery.iconpack.data.db.Db;
import com.sorcerer.sorcery.iconpack.ui.theme.ThemeManager;

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

    @Provides
    @Singleton
    ThemeManager provideThemeManager() {
        return new ThemeManager(mApp);
    }

    @Provides
    @Singleton
    Db provideDb() {
        return new Db(mApp);
    }
}
