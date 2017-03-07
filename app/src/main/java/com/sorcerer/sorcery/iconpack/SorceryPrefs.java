package com.sorcerer.sorcery.iconpack;

import android.content.Context;

import com.f2prateek.rx.preferences2.Preference;
import com.sorcerer.sorcery.iconpack.settings.prefs.Prefs;
import com.sorcerer.sorcery.iconpack.settings.prefs.SorceryPreference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/4
 */

public class SorceryPrefs extends Prefs {

    SorceryPrefs(Context context) {
        super(context);
    }

    @Override
    protected String providePreferenceName() {
        return "SorceryIcons";
    }

    private SorceryPreference<Boolean> mDonated;

    public SorceryPreference<Boolean> donated() {
        if (mDonated == null) {
            mDonated = new SorceryPreference<>(getPreferences(), "donated", false);
        }
        return mDonated;
    }

    private SorceryPreference<Boolean> mDevOptionsOpened;

    public SorceryPreference<Boolean> devOptionsOpened() {
        if (mDevOptionsOpened == null) {
            mDevOptionsOpened =
                    new SorceryPreference<>(getPreferences(), "dev options opened", false);
        }
        return mDevOptionsOpened;
    }

    private Map<String, Preference<Boolean>> tabShowMap = new HashMap<>();

    public Preference<Boolean> isTabShow(String tabName) {
        if (tabShowMap.containsKey(tabName)) {
            return tabShowMap.get(tabName);
        }
        Preference<Boolean> sp =
                getRxSharedPreferences().getBoolean("tab_show_" + tabName, true);
        tabShowMap.put(tabName, sp);
        return sp;
    }

    private SorceryPreference<Boolean> mUserGuideShowed;

    public SorceryPreference<Boolean> userGuideShowed() {
        if (mUserGuideShowed == null) {
            mUserGuideShowed =
                    new SorceryPreference<>(getPreferences(), "user_guide_showed", false);
        }
        return mUserGuideShowed;
    }

    public SorceryPreference<String> getUUID() {
        SorceryPreference<String> uuid = new SorceryPreference<>(getPreferences(), "uuid", "");
        if (uuid.getValue().isEmpty()) {
            String id = UUID.randomUUID().toString();
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
}
