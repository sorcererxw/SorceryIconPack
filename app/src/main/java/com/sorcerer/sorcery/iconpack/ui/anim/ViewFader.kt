package com.sorcerer.sorcery.iconpack.ui.anim

import android.view.View
import android.view.ViewGroup

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/11/4
 */

class ViewFader {
    fun hideContentOf(viewGroup: ViewGroup) {
        for (i in 0..viewGroup.childCount - 1) {
            viewGroup.getChildAt(i).visibility = View.GONE
        }
    }

    fun showContent(viewGroup: ViewGroup) {
        for (i in 0..viewGroup.childCount - 1) {
            viewGroup.getChildAt(i).visibility = View.VISIBLE
        }
    }
}
