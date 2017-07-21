package com.sorcerer.sorcery.iconpack.ui.activities.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.GestureDetector

import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.sorcerer.sorcery.iconpack.ui.callbacks.ToolbarOnGestureListener
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/5/30 0030
 */
abstract class ToolbarActivity : BaseActivity() {
    protected abstract val toolbar: Toolbar?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(this.toolbar != null)
        setSupportActionBar(this.toolbar)

        toolbar!!.background = ColorDrawable(
                ResourceUtil.getAttrColor(this, android.R.attr.colorPrimary))
    }

    protected fun setToolbarCloseIndicator() {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(
                    IconicsDrawable(this, GoogleMaterial.Icon.gmd_close)
                            .sizeDp(24)
                            .paddingDp(4)
                            .color(Color.WHITE))
        }
    }

    protected fun setToolbarBackIndicator() {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected fun setToolbarDoubleTapListener(
            listener: ToolbarOnGestureListener.DoubleTapListener) {
        val detector = GestureDetector(
                this,
                ToolbarOnGestureListener(listener)
        )
        this.toolbar!!.setOnTouchListener { _, event ->
            detector.onTouchEvent(event)
            true
        }
    }
}
