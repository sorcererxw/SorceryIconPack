package com.sorcerer.sorcery.iconpack.customWorkshop

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.view.MenuItem
import butterknife.BindView
import com.sorcerer.sorcery.iconpack.R
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/2/18
 */

class CustomWorkshopActivity : BaseSubActivity() {
    @BindView(R.id.coordinatorLayout_custom)
    internal var mCoordinatorLayout: CoordinatorLayout? = null

    override fun provideLayoutId(): Int {
        return R.layout.activity_custom_workshop
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarBackIndicator()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            super.onBackPressed()
        }
        return false
    }
}
