package com.sorcerer.sorcery.iconpack.settings.licenses.ui;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.settings.licenses.models.OpenSourceLibBean;
import com.sorcerer.sorcery.iconpack.settings.licenses.models.OpenSourceLibInformation;

import java.util.Arrays;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/7
 */

public class OpenSourceLibDialog {
    private static Dialog sDialog;

    public static void show(Activity activity) {
        if (sDialog != null) {
            sDialog.show();
            return;
        }
        List<OpenSourceLibBean> list = Arrays.asList(
                new OpenSourceLibInformation.RxJavaInfoBean(),
                new OpenSourceLibInformation.RxAndroidInfoBean(),
                new OpenSourceLibInformation.RxActivityResultInfoBean(),
                new OpenSourceLibInformation.RxPermissionInfoBean(),
                new OpenSourceLibInformation.XposedBridgeInfoBean(),
                new OpenSourceLibInformation.GlideInfoBean(),
                new OpenSourceLibInformation.MaterialDrawerInfoBean(),
                new OpenSourceLibInformation.MaterialDialogsInfoBean(),
                new OpenSourceLibInformation.SliceInfoBean(),
                new OpenSourceLibInformation.AVLoadingIndicatorViewInfoBean(),
                new OpenSourceLibInformation.PhotoViewInfoBean(),
                new OpenSourceLibInformation.IconicsInfoBean(),
                new OpenSourceLibInformation.ButterknifeInfoBean(),
                new OpenSourceLibInformation.RetrofitInfoBean(),
                new OpenSourceLibInformation.LibsuperuserInfoBean(),
                new OpenSourceLibInformation.GsonInfoBean(),
                new OpenSourceLibInformation.JsoupInfoBean(),
                new OpenSourceLibInformation.LightweightStreamApiInfoBean(),
                new OpenSourceLibInformation.MaterializeInfoBean()
        );

        OpenSourceLibAdapter adapter = new OpenSourceLibAdapter(activity, list);

        RecyclerView recyclerView = new RecyclerView(activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, VERTICAL, false));

        sDialog = new MaterialDialog.Builder(activity)
                .title(R.string.preference_about_lib)
                .positiveText(R.string.action_close)
                .onPositive((dialog, which) -> dialog.dismiss())
                .customView(recyclerView, false)
                .build();

        sDialog.show();
    }
}
