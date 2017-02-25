package com.sorcerer.sorcery.iconpack.iconShowCase.overview

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/19
 */

interface IViewPageFragment {
    fun onSelected()

    /*
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    fun onStateChange(state: Int)
}