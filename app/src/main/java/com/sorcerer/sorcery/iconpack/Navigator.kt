package com.sorcerer.sorcery.iconpack

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sorcerer.sorcery.iconpack.test.TestActivity
import rx_activity_result2.RxActivityResult


/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/11/4
 */

class Navigator(private val mActivity: Activity) {

    fun toSearch() {
        if (mActivity is MainActivity) {
            val mainActivity = mActivity
            RxActivityResult.on(mActivity)
                    .startIntent(Intent(mActivity, SearchActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            .putExtra("custom picker", mainActivity.isCustomPicker))
                    .filter({ activityResult -> activityResult.data() != null })
                    .subscribe({ result ->
                        mainActivity.onReturnCustomPickerRes(
                                result.data().getIntExtra("icon res", 0))
                    })
        } else {
            mActivity.startActivity(Intent(mActivity, SearchActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }

    fun toIconRequest() {
        mainActivityShift(AppSelectActivity::class.java)
    }

    fun toAppleActivity() {
        mainActivityShift(ApplyActivity::class.java)
    }

    fun toDonateActivity() {
        mainActivityShift(DonateActivity::class.java)
    }

    fun toHelpMarkdownActivity() {
        mainActivityShift(HelpActivity::class.java)
    }

    fun toSettingsActivity() {
        mainActivityShift(SettingsActivity::class.java)
    }

    fun toTestActivity() {
        val intent = Intent(mActivity, TestActivity::class.java)
        mActivity.startActivity(intent)
    }

    private fun mainActivityShift(cls: Class<*>) {
        val intent = Intent(mActivity, cls)
        mActivity.startActivity(intent)

//        val lessAnim = mPrefs!!.lessAnim().get()
//        if (lessAnim != null && lessAnim) {
//            mActivity.overridePendingTransition(0, 0)
//        } else {
        mActivity.overridePendingTransition(R.anim.slide_right_in, 0)
//        }
    }

    companion object {

        fun toWebPage(context: Context, url: String) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }

        fun toMail(context: Context, receiver: String, subject: String, content: String): Boolean {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(receiver))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return true
            } else {
                return false
            }
        }

        fun toAppMarketSearchResult(context: Context, query: String) {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://search?q=$query&c=apps"))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/search?q=$query&c=apps"))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }

        }
    }
}
