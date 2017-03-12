package com.sorcerer.sorcery.iconpack.iconShowCase.overview;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/5
 */

interface IViewPageFragment {
    void onSelected();

    /*
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    void onStateChange(int state);
}
