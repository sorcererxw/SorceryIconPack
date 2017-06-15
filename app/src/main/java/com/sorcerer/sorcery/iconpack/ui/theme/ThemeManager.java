package com.sorcerer.sorcery.iconpack.ui.theme;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/12
 */

public class ThemeManager {
    private final ThemePrefs mThemePrefs;
    private BaseTheme mCurrentTheme;
    private Context mContext;
    private final Function<Integer, Integer> COLOR_MAPPER =
            color -> ContextCompat.getColor(mContext, color);

    public ThemeManager(Context context) {
        mContext = context;
        mThemePrefs = new ThemePrefs(context);

        theme().subscribe(theme -> {
            mCurrentTheme = theme;
        }, Timber::e);
    }

    public Observable<BaseTheme> theme() {
        return mThemePrefs.theme().asObservable().map(s -> {
            if (s.equals(DarkTheme.Companion.getNAME())) {
                return new DarkTheme();
            } else {
                return new LightTheme();
            }
        });
    }

    public Observable<Integer> primaryColor() {
        return mThemePrefs.primaryColor().asObservable().map(COLOR_MAPPER);
    }

    private void setPrimaryColor(int color) {
        mThemePrefs.primaryColor().set(color);
    }

    public Observable<Integer> primaryDarkColor() {
        return mThemePrefs.primaryDarkColor().asObservable().map(COLOR_MAPPER);
    }

    private void setPrimaryDarkColor(int color) {
        mThemePrefs.primaryDarkColor().set(color);
    }

    public Observable<Integer> accentColor() {
        return mThemePrefs.accentColor().asObservable().map(COLOR_MAPPER);
    }

    private void setAccentColor(int color) {
        mThemePrefs.accentColor().set(color);
    }

    public Observable<Integer> cardColor() {
        return mThemePrefs.cardColor().asObservable().map(COLOR_MAPPER);
    }

    private void setCardColor(int color) {
        mThemePrefs.cardColor().set(color);
    }

    public Observable<Integer> backgroundColor() {
        return mThemePrefs.backgroundColor().asObservable().map(COLOR_MAPPER);
    }

    private void setBackgroundColor(int color) {
        mThemePrefs.backgroundColor().set(color);
    }

    public Observable<Integer> primaryTextColor() {
        return mThemePrefs.primaryTextColor().asObservable().map(COLOR_MAPPER);
    }

    private void setPrimaryTextColor(int color) {
        mThemePrefs.primaryTextColor().set(color);
    }

    public Observable<Integer> secondaryTextColor() {
        return mThemePrefs.secondaryTextColor().asObservable().map(COLOR_MAPPER);
    }

    private void setSecondaryTextColor(int color) {
        mThemePrefs.secondaryTextColor().set(color);
    }

    public void setTheme(BaseTheme theme) {
        if (theme.name().equals(mCurrentTheme.name())) {
            return;
        }

        setPrimaryColor(theme.primaryColor());
        setPrimaryDarkColor(theme.primaryDarkColor());
        setAccentColor(theme.accentColor());
        setCardColor(theme.cardColor());
        setBackgroundColor(theme.backgroundColor());
        setPrimaryTextColor(theme.primaryTextColor());
        setSecondaryTextColor(theme.secondaryTextColor());

        mThemePrefs.theme().set(theme.name());
    }

    public void setTheme(Activity activity) {
        activity.setTheme(mCurrentTheme.style());
    }

    public BaseTheme getCurrentTheme() {
        return mCurrentTheme;
    }
}
