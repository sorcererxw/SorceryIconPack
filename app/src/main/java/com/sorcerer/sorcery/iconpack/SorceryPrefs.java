package com.sorcerer.sorcery.iconpack;

import android.content.Context;
import android.provider.Settings;

import com.f2prateek.rx.preferences2.Preference;
import com.sorcerer.sorcery.iconpack.settings.prefs.Prefs;
import com.sorcerer.sorcery.iconpack.settings.prefs.SorceryPreference;

import java.util.HashMap;
import java.util.UUID;

import timber.log.Timber;

/**
 * @description: *
 * @author: Sorcerer
 * *
 * @date: 2016/10/4
 */

public class SorceryPrefs extends Prefs {

    private HashMap<String, Preference<Boolean>> tabShowMap = new HashMap<>();

    SorceryPrefs(Context context) {
        super(context);
    }

    @Override
    protected String providePreferenceName() {
        return "SorceryIcons";
    }

    public SorceryPreference<Boolean> donated() {
        return new SorceryPreference<>(getPreferences(), "donated", false);
    }

    public SorceryPreference<Boolean> devOptionsOpened() {
        return new SorceryPreference<>(getPreferences(), "dev options opened", false);
    }

    public Preference<Boolean> isTabShow(String tabName) {
        if (tabShowMap.containsKey(tabName)) {
            return tabShowMap.get(tabName);
        }
        Preference<Boolean> sp = getRxSharedPreferences().getBoolean("tab_show_" + tabName, true);
        tabShowMap.put(tabName, sp);
        return sp;
    }

    public SorceryPreference<Boolean> userGuideShowed() {
        return new SorceryPreference<>(getPreferences(), "user_guide_showed", false);
    }

    public SorceryPreference<String> getUuid() {
        SorceryPreference<String> uuid = new SorceryPreference<>(getPreferences(), "uuid", "");
        if (uuid.getValue().isEmpty()) {
            String id;
            try {
                id = Settings.Secure.getString(mContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                if (id == null || id.isEmpty()) {
                    id = UUID.randomUUID().toString();
                }
            } catch (Exception e) {
                Timber.e(e);
                id = UUID.randomUUID().toString();
            }

            uuid.setValue(id, true);
        }
        return uuid;
    }

    public SorceryPreference<Boolean> isPixelDisableRound() {
        return new SorceryPreference<>(getPreferences(), "pixel_disable_round", false);
    }

    public SorceryPreference<Boolean> isPixelDisableWork() {
        return new SorceryPreference<>(getPreferences(), "pixel_disable_work", false);
    }

    public SorceryPreference<Boolean> isPixelNormalizeIconSize() {
        return new SorceryPreference<>(getPreferences(), "pixel_normalize_icon_size", false);
    }

    public SorceryPreference<Boolean> isGoogleNowDisableWork() {
        return new SorceryPreference<>(getPreferences(), "google_now_disable_work", false);
    }

    public Preference<Boolean> pixelIconSkip(String packageName) {
        return getRxSharedPreferences().getBoolean("pixel_icon_skip_" + packageName, false);
    }

    public Preference<Boolean> lessAnim() {
        return getRxSharedPreferences().getBoolean("less_animation", false);
    }

    public Preference<Boolean> enableTransparentNavBar() {
        return getRxSharedPreferences().getBoolean("enable_transparent_nav_bar", false);
    }


    public SorceryPreference<Boolean> hideItself() {
        return new SorceryPreference<>(getPreferences(), "hide_itself", false);
    }

    public Preference<Boolean> firstTimeLaunch() {
        return getRxSharedPreferences().getBoolean("first_time_launch", true);
    }

    public Boolean nightMode() {
        return getRxSharedPreferences().getBoolean("night_mode", false).get();
    }

    public void nightMode(Boolean enable) {
        getRxSharedPreferences().getBoolean("night_mode").set(enable);
    }
}
