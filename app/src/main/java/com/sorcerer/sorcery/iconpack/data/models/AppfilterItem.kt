package com.sorcerer.sorcery.iconpack.data.models

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/11/29
 */

class AppfilterItem(var component: String, var drawable: String) {
    val packageName: String
        get() = component.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
}
