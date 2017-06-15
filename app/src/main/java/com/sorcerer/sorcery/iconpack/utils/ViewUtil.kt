package com.sorcerer.sorcery.iconpack.utils

import android.view.View

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/4/27
 */
object ViewUtil {
    fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]

        return x > viewX && x < viewX + view.width && y > viewY && y < viewY + view.height
    }

    fun isClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val differenceX = Math.abs(startX - endX)
        val differenceY = Math.abs(startY - endY)
        return !(differenceX > 5 || differenceY > 5)
    }

}
