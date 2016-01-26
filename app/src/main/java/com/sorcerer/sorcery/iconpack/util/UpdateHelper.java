package com.sorcerer.sorcery.iconpack.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.FirVersion;

import org.json.JSONException;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by Sorcerer on 2016/1/26 0026.
 */
public class UpdateHelper {
    private Context mContext;

    public UpdateHelper(final Context context) {
        mContext = context;
    }


    public void update() {
        FIR.checkForUpdateInFIR(mContext.getString(R.string.fir_token), new VersionCheckCallback() {
            @Override
            public void onSuccess(String versionJson) {
                try {
                    final FirVersion firVersion = new FirVersion(versionJson);
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
                    builder.title(mContext.getString(R.string.find_new_version))
                            .content(firVersion.getBuild() + "\n" + firVersion
                                    .getChangelog())
                            .onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog,
                                                    @NonNull DialogAction which) {
                                    if (which == DialogAction.POSITIVE) {
                                        Utility.downloadFile(mContext,
                                                firVersion.getInstallUrl());
                                    } else if (which == DialogAction.NEUTRAL) {

                                    } else if (which == DialogAction.NEGATIVE) {

                                    }
                                }
                            }).positiveText(mContext.getString(R.string.download)).negativeText
                            (mContext.getString(R.string.cancel)).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Exception exception) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }

}
