package com.sorcerer.sorcery.iconpack.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;

import java.util.List;

/**
 * Created by Sorcerer on 2016/4/24.
 */
public class MailUtil {

    private Context mContext;

    public MailUtil(Context context){
        mContext =context;
    }

    public interface SendMailCallback {
        void onSuccess();

        void onFail();
    }

    private void send(String sendString, MailSenderInfo mailInfo,
                      SendMailCallback callback) {
        try {
            SendMailAsyncTask myAsyncTask = new SendMailAsyncTask(mContext);
            if (!sendString.isEmpty()) {
                myAsyncTask.setStringToSend(sendString);
            }
            myAsyncTask.execute(mailInfo);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    private class SendMailAsyncTask extends AsyncTask<MailSenderInfo, Integer, Boolean> {
        private ProgressDialog mProgressDialog;
        private Context mContext;
        private String stringToSend = "";
        private SendMailCallback mSendMailCallback;

        public void setSendMailCallback(SendMailCallback callback) {
            mSendMailCallback = callback;
        }


        public SendMailAsyncTask(Context context) {
            mContext = context;
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        public void setStringToSend(String s) {
            stringToSend = s;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage(mContext.getString(R.string.icon_request_sending));
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (success && mSendMailCallback != null) {
                mSendMailCallback.onSuccess();
            } else if (mSendMailCallback != null) {
                mSendMailCallback.onFail();
            }
        }

        @Override
        protected Boolean doInBackground(MailSenderInfo... params) {
            String s = "";
//            List list = mAdapter.getCheckedAppsList();
//            for (int i = 0; i < list.size(); i++) {
//                s += list.get(i).toString();
//                s += "------------------------------\n";
//            }
            try {
                params[0].setContent(stringToSend + "\n\n" + s);
                SimpleMailSender sms = new SimpleMailSender();
                sms.sendTextMail(params[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
