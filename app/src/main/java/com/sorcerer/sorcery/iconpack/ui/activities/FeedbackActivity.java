package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;
import com.sorcerer.sorcery.iconpack.util.SimpleMailSender;
import com.sorcerer.sorcery.iconpack.util.Utility;

import java.util.List;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Toolbar mToolbar;
    private Button mRequestButton;
    private Button mSuggestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar_feedback);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mRequestButton = (Button) findViewById(R.id.button_request);
        mSuggestButton = (Button) findViewById(R.id.button_suggest);

        mRequestButton.setOnClickListener(this);
        mSuggestButton.setOnClickListener(this);

        (findViewById(R.id.textView_feedback_explain))
                .setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager)
                                getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("feedback mailbox", getString(R.string
                                .feedback_mailbox));
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(FeedbackActivity.this,
                                "copied to your clipboard :)",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(!Utility.isNetworkAvailable(this)){
            Toast.makeText(mContext, "network error", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id == R.id.button_request) {
            String s = "";
            List list = Utility.getComponentInfo(this);
            for (int i = 0; i < list.size(); i++) {
                s += list.get(i).toString();
                s += "------------------------------\n";
            }
//            Intent i = new Intent(Intent.ACTION_SEND);
//            i.setType("message/rfc822");
//            i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_mailbox)});
//            i.putExtra(Intent.EXTRA_SUBJECT, "Sorcery icon pack: icon request");
//            i.putExtra(Intent.EXTRA_TEXT, s);
//            try {
//                startActivity(Intent.createChooser(i, getString(R.string.choose_mail_app)));
//            } catch (android.content.ActivityNotFoundException ex) {
//                Toast.makeText(this,
//                        "There are no email clients installed.",
//                        Toast.LENGTH_SHORT).show();
//            }
            try {
                MailSenderInfo mailInfo = new MailSenderInfo();
                mailInfo.setMailServerHost("smtp.qq.com");
                mailInfo.setMailServerPort("25");
                mailInfo.setValidate(true);
                //                mailInfo.setUserName("feedback-sorcerer@qq.com");
                //                mailInfo.setPassword("feedback");
                //                mailInfo.setFromAddress("feedback-sorcerer@qq.com");
                mailInfo.setUserName(getString(R.string.feedback_mailbox));
                mailInfo.setPassword(getString(R.string.feedback_mail_password));
                mailInfo.setFromAddress(getString(R.string.feedback_mailbox));
                mailInfo.setToAddress("pqyljn@hotmail.com");
                mailInfo.setSubject("Feedback-SmartSmsHelper");
                mailInfo.setContent(s);
                SendMailAsyncTask myAsyncTask = new SendMailAsyncTask();
                myAsyncTask.execute(mailInfo);
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }
        } else if (id == R.id.button_suggest) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_mailbox)});
            i.putExtra(Intent.EXTRA_SUBJECT, "Sorcery icon pack: icon request");
            i.putExtra(Intent.EXTRA_TEXT, "write down your suggestion:\n");
            try {
                startActivity(Intent.createChooser(i, getString(R.string.choose_mail_app)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this,
                        "There are no email clients installed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SendMailAsyncTask extends AsyncTask<MailSenderInfo, Integer, Boolean> {
        private ProgressDialog mProgressDialog;

        public SendMailAsyncTask() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Progress start");
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (success) {
                Toast.makeText(mContext, "send success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "send fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(MailSenderInfo... params) {
            try {
                SimpleMailSender sms = new SimpleMailSender();
                sms.sendTextMail(params[0]);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private String organizeMail() {
        String msg = "";
        msg += "\n----------------------------------------\n";
        msg += "\nProduct: " + android.os.Build.PRODUCT;
        msg += "\nCPU_ABI: " + android.os.Build.CPU_ABI;
        msg += "\nTAGS: " + android.os.Build.TAGS;
        msg += "\nVERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
        msg += "\nMODEL: " + android.os.Build.MODEL;
        msg += "\nSDK: " + android.os.Build.VERSION.SDK;
        msg += "\nVERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
        msg += "\nDEVICE: " + android.os.Build.DEVICE;
        msg += "\nDISPLAY: " + android.os.Build.DISPLAY;
        msg += "\nBRAND: " + android.os.Build.BRAND;
        msg += "\nBOARD: " + android.os.Build.BOARD;
        msg += "\nFINGERPRINT: " + android.os.Build.FINGERPRINT;
        msg += "\nID: " + android.os.Build.ID;
        msg += "\nMANUFACTURER: " + android.os.Build.MANUFACTURER;
        msg += "\nUSER: " + android.os.Build.USER;

        return msg;
    }
}
