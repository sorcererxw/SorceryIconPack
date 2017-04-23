package com.sorcerer.sorcery.iconpack.settings.about;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.Navigator;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.utils.Dialogs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/7
 */

public class AboutDialog {

    public static void show(Activity activity) {
        show(activity, null);
    }

    public static void show(Activity activity, View.OnTouchListener logoOnTouchListener) {
        View view = View.inflate(activity, R.layout.layout_about_dialog, null);
        ImageView logoImageView = (ImageView) view.findViewById(R.id.imageView_about_dialog_logo);

        if (logoOnTouchListener != null) {
            logoImageView.setOnTouchListener(logoOnTouchListener);
        }

        view.findViewById(R.id.imageView_about_dialog_social_google_plus)
                .setOnClickListener(v -> Navigator.toWebPage(activity,
                        "https://plus.google.com/communities/115317471515103046699"));
        view.findViewById(R.id.imageView_about_dialog_social_github)
                .setOnClickListener(v -> Navigator.toWebPage(activity,
                        "https://github.com/sorcererXW/SorceryIconPack"));
        view.findViewById(R.id.imageView_about_dialog_social_coolapk)
                .setOnClickListener(v -> Navigator.toWebPage(activity,
                        "http://www.coolapk.com/apk/com.sorcerer.sorcery.iconpack"));
        TextView versionTextView = (TextView) view.findViewById(R.id.textView_about_dialog_version);
        TextView titleTextView = (TextView) view.findViewById(R.id.textView_about_dialog_title);
        TextView creditsTextView = (TextView) view.findViewById(R.id.textView_about_dialog_credits);
        TextView iconCountTextView =
                (TextView) view.findViewById(R.id.textView_about_dialog_icon_count);

        iconCountTextView.setText(String.format(
                ResourceUtil.getString(activity, R.string.about_dialog_icon_count),
                Stream.of(ResourceUtil.getStringArray(activity, R.array.icon_pack))
                        .filter(value -> !value.startsWith("**"))
                        .count()
                )
        );

        versionTextView.setText(String.format("%s: %s",
                ResourceUtil.getString(activity, R.string.version),
                BuildConfig.VERSION_NAME));
        titleTextView.setTypeface(
                Typeface.createFromAsset(activity.getAssets(), "RockwellStd.otf"));
        SpannableString openSource =
                new SpannableString(activity.getString(R.string.open_source_lib));
        openSource.setSpan(new UnderlineSpan(), 0, openSource.length(), 0);
        String htmlBuilder = "";
        htmlBuilder +=
                ("<a>" + ResourceUtil.getString(activity, R.string.contributor) + "</a><br>");
        htmlBuilder += ("<a href=\"https://github.com/sorcererxw\">SorcererXW</a><br>");
        htmlBuilder += ("<a href=\"http://weibo.com/mozartjac\">翟宅宅Jack</a><br>");
        htmlBuilder += ("<a href=\"http://www.coolapk.com/u/432987\">Tarrant Yan</a><br>");
        htmlBuilder += ("<a>nako liu</a>");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            creditsTextView.setText(Html.fromHtml(htmlBuilder, Html.FROM_HTML_MODE_LEGACY));
        } else {
            creditsTextView.setText(Html.fromHtml(htmlBuilder));
        }
        creditsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        Dialog sDialog = Dialogs.builder(activity)
                .customView(view, true)
                .build();

        sDialog.show();
    }
}
