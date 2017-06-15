package com.sorcerer.sorcery.iconpack.settings.about

import android.app.Activity
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.annimon.stream.Stream
import com.sorcerer.sorcery.iconpack.BuildConfig
import com.sorcerer.sorcery.iconpack.Navigator
import com.sorcerer.sorcery.iconpack.R
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/2/7
 */

object AboutDialog {

    @Suppress("DEPRECATION")
    @JvmOverloads fun show(activity: Activity, logoOnTouchListener: View.OnTouchListener? = null) {
        val view = View.inflate(activity, R.layout.layout_about_dialog, null)
        val logoImageView = view.findViewById<ImageView>(R.id.imageView_about_dialog_logo)

        if (logoOnTouchListener != null) {
            logoImageView.setOnTouchListener(logoOnTouchListener)
        }

        view.findViewById<View>(R.id.imageView_about_dialog_social_google_plus).setOnClickListener {
            Navigator.toWebPage(activity,
                    "https://plus.google.com/communities/115317471515103046699")
        }
        view.findViewById<View>(R.id.imageView_about_dialog_social_github).setOnClickListener {
            Navigator.toWebPage(activity, "https://github.com/sorcererXW/SorceryIconPack")
        }
        view.findViewById<View>(R.id.imageView_about_dialog_social_coolapk).setOnClickListener {
            Navigator.toWebPage(activity,
                    "http://www.coolapk.com/apk/com.sorcerer.sorcery.iconpack")
        }
        val versionTextView = view.findViewById<TextView>(R.id.textView_about_dialog_version)
        val creditsTextView = view.findViewById<TextView>(R.id.textView_about_dialog_credits)
        val iconCountTextView = view.findViewById<TextView>(R.id.textView_about_dialog_icon_count)
        iconCountTextView.text = String.format(
                ResourceUtil.getString(activity, R.string.about_dialog_icon_count),
                Stream.of(*ResourceUtil.getStringArray(activity, R.array.icon_pack))
                        .filter { value -> !value.startsWith("**") }
                        .count()
        )

        versionTextView.text = String.format("%s: %s",
                ResourceUtil.getString(activity, R.string.version),
                BuildConfig.VERSION_NAME)
        val openSource = SpannableString(activity.getString(R.string.open_source_lib))
        openSource.setSpan(UnderlineSpan(), 0, openSource.length, 0)
        val htmlBuilder = StringBuffer()
                .append("<a>" + ResourceUtil.getString(activity, R.string.contributor) + "</a><br>")
                .append("<a href=\"https://github.com/sorcererxw\">SorcererXW</a><br>")
                .append("<a href=\"http://weibo.com/mozartjac\">翟宅宅Jack</a><br>")
                .append("<a href=\"http://www.coolapk.com/u/432987\">Tarrant Yan</a><br>")
                .append("<a>nako liu</a>")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            creditsTextView.text = Html.fromHtml(htmlBuilder.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            creditsTextView.text = Html.fromHtml(htmlBuilder.toString())
        }
        creditsTextView.movementMethod = LinkMovementMethod.getInstance()
        val dialog = Dialogs.builder(activity).customView(view, true).build()

        dialog.show()
    }
}
