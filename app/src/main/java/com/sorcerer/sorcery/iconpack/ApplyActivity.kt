package com.sorcerer.sorcery.iconpack

import android.os.Bundle
import android.support.v4.app.Fragment

import com.sorcerer.sorcery.iconpack.apply.OverviewFragment
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseFragmentSubActivity

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/12/2
 */

class ApplyActivity : BaseFragmentSubActivity() {


    override fun provideLayoutId(): Int {
        return R.layout.activity_apply
    }

    override fun provideFragmentContainer(): Int {
        return R.id.frameLayout_apply_fragment_container
    }

    override fun provideInitFragment(): Fragment {
        return OverviewFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarBackIndicator()

    }

}