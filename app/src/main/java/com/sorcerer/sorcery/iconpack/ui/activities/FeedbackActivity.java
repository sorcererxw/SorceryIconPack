package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.RequestAdapter;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;
import com.sorcerer.sorcery.iconpack.util.SimpleMailSender;
import com.sorcerer.sorcery.iconpack.util.Utility;

import java.util.List;

public class FeedbackActivity extends SlideInAndOutAppCompatActivity
        implements View.OnClickListener {

    private Context mContext;
    private Toolbar mToolbar;
    private Button mRequestButton;
    private Button mSuggestButton;

    private MaterialDialog.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar_universal);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

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
        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(mContext, "network error", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id == R.id.button_request) {
//            try {
//                MailSenderInfo mailInfo = new MailSenderInfo();
//                mailInfo.setMailServerHost("smtp.163.com");
//                mailInfo.setMailServerPort("25");
//
//                mailInfo.setValidate(true);
//                mailInfo.setUserName(getString(R.string.feedback_mailbox));
//                mailInfo.setPassword(getString(R.string.feedback_mail_password));
//                mailInfo.setFromAddress(getString(R.string.feedback_mailbox));
//                mailInfo.setToAddress(getString(R.string.feedback_receive_mailbox));
//                mailInfo.setSubject("icon request");
//                SendMailAsyncTask myAsyncTask = new SendMailAsyncTask();
//                myAsyncTask.execute(mailInfo);
//            } catch (Exception e) {
//                Log.e("SendMail", e.getMessage(), e);
//            }
            showRequestDialog();
        } else if (id == R.id.button_suggest) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:" + getString(R.string.feedback_receive_mailbox)));
            i.putExtra(Intent.EXTRA_SUBJECT, "Sorcery icon pack: suggest");
            i.putExtra(Intent.EXTRA_TEXT, "write down your suggestion:\n");
            startActivity(i);
        }
    }

    private class SendMailAsyncTask extends AsyncTask<MailSenderInfo, Integer, Boolean> {
        private ProgressDialog mProgressDialog;

        public SendMailAsyncTask() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage(getString(R.string.icon_request_sending));
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (success) {
                Snackbar.make(findViewById(R.id.relativeLayout_feedback_root), getString(R.string
                                .icon_request_send_success),
                        Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(findViewById(R.id.relativeLayout_feedback_root), getString(R.string
                                .icon_request_send_fail),
                        Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        protected Boolean doInBackground(MailSenderInfo... params) {
            String s = "";
            List list = Utility.getComponentInfo(getApplicationContext());
            for (int i = 0; i < list.size(); i++) {
                s += list.get(i).toString();
                s += "------------------------------\n";
            }
            try {
                params[0].setContent(s);
                SimpleMailSender sms = new SimpleMailSender();
                sms.sendTextMail(params[0]);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private class LoadAppsAsyncTask extends AsyncTask {

        private Context mContext;

        public LoadAppsAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBuilder = new MaterialDialog.Builder(mContext);
            mBuilder.showListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            mBuilder.customView(LayoutInflater.from(mContext).inflate(R.layout
                    .layout_icon_request_loading, null), true);
            mBuilder.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return Utility.getComponentInfo(mContext);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            List<AppInfo> appInfoList = (List<AppInfo>) o;

            View view = LinearLayout
                    .inflate(mContext, R.layout.layout_icon_request, null);
            RecyclerView recyclerView =
                    (RecyclerView) view.findViewById(R.id.recyclerView_icon_request);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(mContext,
                            LinearLayoutManager.VERTICAL,
                            false)
            );
            recyclerView.setAdapter(
                    new RequestAdapter(mContext,
                            appInfoList));
            mBuilder.customView(view, true);
            mBuilder.onAny(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                }
            });
            mBuilder.positiveText("send");
            mBuilder.negativeText("cancel");
            mBuilder.show();
        }
    }


    private void showRequestDialog() {/*
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.cancelable(false);
//        builder.keyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (KeyEvent.KEYCODE_BACK == keyCode) {
//                    dialog.dismiss();
//                }
//                return false;
//            }
//        });
        View view = new RequestDialogView(this);
//        View view = LinearLayout.inflate(this, R.layout.layout_icon_request, null);
//        RecyclerView recyclerView =
//                (RecyclerView) view.findViewById(R.id.recyclerView_icon_request);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(
//                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        );
//        recyclerView.setAdapter(
//                new RequestAdapter(this,Utility.getComponentInfo(getApplicationContext()))
//        );
        builder.customView(view, true);
        builder.onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            }
        });
        builder.positiveText("send");
        builder.negativeText("cancel");
        builder.show();*/
//        LoadAppsAsyncTask appsAsyncTask = new LoadAppsAsyncTask(this);
//        appsAsyncTask.execute();
        Intent intent = new Intent(this, AppSelectActivity.class);
        startActivity(intent);
    }
}
