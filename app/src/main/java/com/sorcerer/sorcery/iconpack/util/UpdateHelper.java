package com.sorcerer.sorcery.iconpack.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.FirVersion;
import com.sorcerer.sorcery.iconpack.ui.activities.ApplyActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.zip.Inflater;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by Sorcerer on 2016/1/26 0026.
 */
public class UpdateHelper {
    private Context mContext;
    private View mRootView;

    public UpdateHelper(final Context context) {
        mContext = context;
    }

    public UpdateHelper(final Context context, View rootView) {
        mContext = context;
        mRootView = rootView;
    }

    public void update() {
        FIR.checkForUpdateInFIR(mContext.getString(R.string.fir_token), new VersionCheckCallback() {
            @Override
            public void onSuccess(String versionJson) {
                Log.d("sip", versionJson);
                try {
                    final FirVersion firVersion = new FirVersion(versionJson);
                    PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext
                            .getPackageName(), 0);
                    if (Integer.parseInt(firVersion.getBuild()) > packageInfo.versionCode) {
                        showUpdateDialog(firVersion);
                    } else {
                        if (mRootView != null) {
                            Snackbar.make(mRootView, mContext.getString(R.string.is_last_version),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException | PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Exception exception) {
                if (mRootView != null) {
                    Toast.makeText(mContext,
                            mContext.getString(R.string.update_fail),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStart() {
                if (mRootView != null) {
                    Toast.makeText(mContext,
                            mContext.getString(R.string.update_start),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }



    private void showUpdateDialog(final FirVersion firVersion) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        builder.title(mContext.getString(R.string.find_new_version))
                .content(mContext.getString(R.string.version_name) + ": " +
                        firVersion.getVersionShort() +
                        "\n" +
                        firVersion.getChangelog())
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            if (Build.VERSION.SDK_INT >= 23 && !PermissionsHelper.hasPermission
                                    ((Activity) mContext,
                                            PermissionsHelper.WRITE_EXTERNAL_STORAGE_MANIFEST)) {
                                PermissionsHelper.requestWriteExternalStorage((Activity) mContext);
                            } else {
                                download(firVersion);
                            }
                        } else if (which == DialogAction.NEUTRAL) {
                        } else if (which == DialogAction.NEGATIVE) {
                        }
                        dialog.dismiss();
                    }

                })
                .positiveText(mContext.getString(R.string.download))
                .negativeText(mContext.getString(R.string.cancel))
                .show();
    }

    public void download(FirVersion firVersion) {
        Utility.downloadFile(mContext,
                "http://7xqlpg.com1.z0.glb.clouddn" +
                        ".com/sorcery%20icon%20pack"+firVersion.getBuild()+".apk",
                firVersion.getName() + firVersion.getVersionShort() +
                        "" +
                        ".apk");
    }

}
