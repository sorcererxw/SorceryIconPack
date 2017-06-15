package com.sorcerer.sorcery.iconpack

import android.os.Bundle
import android.support.v4.app.Fragment
import com.sorcerer.sorcery.iconpack.settings.SettingsFragment
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseFragmentSubActivity

class SettingsActivity : BaseFragmentSubActivity() {

    override fun provideLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun provideFragmentContainer(): Int {
        return R.id.frameLayout_settings_fragment_container
    }

    override fun provideInitFragment(): Fragment {
        return SettingsFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarBackIndicator()
    }

}
