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

    private UpdateListener mUpdateListener;

    public interface UpdateListener {
        void onSuccess(FirVersion firVersion);

        void onFail();

        void onStart();

        void onFinish();
    }

    public UpdateHelper(final Context context) {
        mContext = context;
        mUpdateListener = new UpdateListener() {
            @Override
            public void onSuccess(FirVersion firVersion) {
                Toast.makeText(context, "suuccess", Toast.LENGTH_SHORT).show();
                MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
                builder.title("find new version").content(firVersion.getBuild() + "\n" + firVersion
                        .getChangelog()).onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {

                        } else if (which == DialogAction.NEUTRAL) {

                        } else if (which == DialogAction.NEGATIVE) {

                        }
                    }
                }).positiveText("download").negativeText("cancel").neutralText("more").show();
            }

            @Override
            public void onFail() {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                Toast.makeText(context, "Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Toast.makeText(context, "finish", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private UpdateHelper(Context context, UpdateListener listener) {
        mContext = context;
        mUpdateListener = listener;
    }

    public void update() {
        FIR.checkForUpdateInFIR(mContext.getString(R.string.fir_token), new VersionCheckCallback() {
            @Override
            public void onSuccess(String versionJson) {
                if (mUpdateListener != null) {
                    try {
                        mUpdateListener.onSuccess(new FirVersion(versionJson));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("fir", "check from fir.im success! " + "\n" + versionJson);
            }

            @Override
            public void onFail(Exception exception) {
                if (mUpdateListener != null) {
                    mUpdateListener.onFail();
                }
                Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
            }

            @Override
            public void onStart() {
                if (mUpdateListener != null) {
                    mUpdateListener.onStart();
                }
            }

            @Override
            public void onFinish() {
                if (mUpdateListener != null) {
                    mUpdateListener.onFinish();
                }
            }
        });
    }

}
