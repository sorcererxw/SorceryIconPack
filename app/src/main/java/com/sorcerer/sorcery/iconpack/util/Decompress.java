package com.sorcerer.sorcery.iconpack.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;
import com.sorcerer.sorcery.iconpack.ui.activities.AppSelectActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Sorcerer on 2016/3/7 0007.
 */
public class Decompress {
    private String _zipFile;
    private String _location;

    public Decompress(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;

        _dirChecker("");
    }

    public void run(Context c) {
        (new DecompressAsyncTask(c)).execute();
    }

    public void unzip() {
        try {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    _dirChecker(ze.getName());
                } else {
                    File file = new File(_location + ze.getName());
                    File f = new java.io.File(file.getParent());
                    f.mkdirs();
                    file.canRead();
                    Log.d("TAG", ze.getName() + ": " + file.canRead());
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
//                    for (int c = zin.read(); c != -1; c = zin.read()) {
//                        fout.write(c);
//                    }
                    byte b[] = new byte[1024];
                    int c;
                    while ((c = zin.read(b,0,1024)) >= 0) {
                        fout.write(b,0,c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch (Exception e) {
            Log.e("Decompress", "unzip", e);
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    private class DecompressAsyncTask extends AsyncTask<MailSenderInfo, Integer, Boolean> {
        private ProgressDialog mProgressDialog;
        private Context mContext;

        public DecompressAsyncTask(Context context) {
            mContext = context;
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        @Override
        protected Boolean doInBackground(MailSenderInfo... params) {
            unzip();
            return true;
        }
    }

}
