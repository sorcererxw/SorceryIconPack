package com.sorcerer.sorcery.iconpack

import android.content.Context
import android.provider.Settings
import com.f2prateek.rx.preferences2.Preference
import com.sorcerer.sorcery.iconpack.settings.prefs.Prefs
import com.sorcerer.sorcery.iconpack.settings.prefs.SorceryPreference
import timber.log.Timber
import java.util.*

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/10/4
 */

class SorceryPrefs internal constructor(context: Context) : Prefs(context) {

    override fun providePreferenceName(): String {
        return "SorceryIcons"
    }

    fun donated(): SorceryPreference<Boolean> {
        return SorceryPreference(preferences, "donated", false)
    }

    fun devOptionsOpened(): SorceryPreference<Boolean> {
        return SorceryPreference(preferences, "dev options opened", false)
    }

    private val tabShowMap = HashMap<String, Preference<Boolean>>()

    fun isTabShow(tabName: String): Preference<Boolean> {
        if (tabShowMap.containsKey(tabName)) {
            return tabShowMap[tabName] as Preference<Boolean>
        }
        val sp = rxSharedPreferences.getBoolean("tab_show_" + tabName, true)
        tabShowMap.put(tabName, sp)
        return sp
    }

    fun userGuideShowed(): SorceryPreference<Boolean> {
        return SorceryPreference(preferences, "user_guide_showed", false)
    }

    val uuid: SorceryPreference<String>
        get() {
            val uuid = SorceryPreference(preferences, "uuid", "")
            if (uuid.value.isEmpty()) {
                var id: String?
                try {
                    id = Settings.Secure.getString(mContext.contentResolver,
                            Settings.Secure.ANDROID_ID)
                    if (id == null || id.isEmpty()) {
                        id = UUID.randomUUID().toString()
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    id = UUID.randomUUID().toString()
                }

                uuid.setValue(id, true)
            }
            return uuid
        }

    val isPixelDisableRound: SorceryPreference<Boolean>
        get() = SorceryPreference(preferences, "pixel_disable_round", false)

    val isPixelDisableWork: SorceryPreference<Boolean>
        get() = SorceryPreference(preferences, "pixel_disable_work", false)

    val isPixelNormalizeIconSize: SorceryPreference<Boolean>
        get() = SorceryPreference(preferences, "pixel_normalize_icon_size", false)

    val isGoogleNowDisableWork: SorceryPreference<Boolean>
        get() = SorceryPreference(preferences, "google_now_disable_work", false)

    fun pixelIconSkip(packageName: String): Preference<Boolean> {
        return rxSharedPreferences.getBoolean("pixel_icon_skip_" + packageName, false)
    }

    fun lessAnim(): Preference<Boolean> {
        return rxSharedPreferences.getBoolean("less_animation", false)
    }

    fun enableTransparentNavBar(): Preference<Boolean> {
        return rxSharedPreferences.getBoolean("enable_transparent_nav_bar", false)
    }


    fun hideItself(): SorceryPreference<Boolean> {
        return SorceryPreference(preferences, "hide_itself", false)
    }

    fun firstTimeLaunch(): Preference<Boolean> {
        return rxSharedPreferences.getBoolean("first_time_launch", true)
    }

    fun nightMode(): Boolean {
        val nightMode: Boolean? = rxSharedPreferences.getBoolean("night_mode", false).get()
                ?: return false
        return nightMode!!
    }

    fun nightMode(enable: Boolean) {
        rxSharedPreferences.getBoolean("night_mode").set(enable)
    }
}
