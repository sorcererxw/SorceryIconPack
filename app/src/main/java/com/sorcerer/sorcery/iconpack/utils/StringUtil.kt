package com.sorcerer.sorcery.iconpack.utils

import timber.log.Timber

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/4/26
 */
object StringUtil {
    fun isMail(mail: String): Boolean {
        return mail.matches(
                "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0_9_-]{2,3}){1,2})$".toRegex())
    }

    fun handleLongXmlString(s: String): String {
        var res = s.replace("|", "\n")
        res = res.replace("#", "    ")
        return res
    }

    fun componentInfoToPackageName(componentInfo: String): String? {
        try {
            return componentInfo.split(
                    "/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split(
                    "\\{".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        } catch (e: Exception) {
            Timber.e(e)
        }

        return null
    }

}
