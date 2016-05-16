package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.RequestAdapter;
import com.sorcerer.sorcery.iconpack.databinding.ActivityAppSelectBinding;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;
import com.sorcerer.sorcery.iconpack.ui.views.MyFloatingActionButton;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.util.MailUtil;
import com.sorcerer.sorcery.iconpack.util.PayHelper;
import com.sorcerer.sorcery.iconpack.util.SimpleMailSender;
import com.sorcerer.sorcery.iconpack.util.StringUtil;
import com.sorcerer.sorcery.iconpack.util.ToolbarOnGestureListener;
import com.sorcerer.sorcery.iconpack.util.Utility;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import c.b.BP;

public class AppSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private RecyclerView mRecyclerView;
    private RequestAdapter mAdapter;
    private AVLoadingIndicatorView mIndicatorView;
    private boolean mCheckAll = false;
    private boolean menuEnable;
    private Menu mMenu;
    private boolean mPremium = false;
    private Activity mActivity = this;
    private boolean mLoadOk;
    private ActivityAppSelectBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout
                .activity_app_select);

        setSupportActionBar(mBinding.toolbarUniversal);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        setToolbarDoubleTap(mBinding.toolbarUniversal);


        try {
            BP.init(mContext, getString(R.string.bmob_app_id));
            mLoadOk = true;
        } catch (Exception e) {
            mLoadOk = false;
        }


        mRecyclerView = mBinding.recyclerViewAppSelect;

        mIndicatorView = mBinding.avLoadingIndicatorViewIconSelect;

        mBinding.setFabListener(this);

        menuEnable = false;
        new LoadAppsAsyncTask(this).execute();
    }

    @Override
    public void onClick(View v) {
        if (mPremium) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
            builder.title(getString(R.string.premium_send_title));
            String s = "";
            final List<String> list = mAdapter.getSelectedAppsNameList();
            for (int i = 0; i < list.size(); i++) {
                s += "<li>" + list.get(i) + "</li><br/>";
            }
            final int amount = list.size() * 2;

            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_premium_custom_info_input, null);
            TextView text = (TextView) view.findViewById(R.id.textView_premium_info_text);
            text.setText(Html.fromHtml(
                    "<p>" + getString(R.string.premium_send_content).replace("|", "<br>") +
                            "</p><ul>" +
                            s +
                            "</ul>" + "<br><b>" + amount + getString(R.string.RMB) + "</b>")
            );
            final EditText email =
                    (EditText) view.findViewById(R.id.materialEditText_premium_info_email);
            final EditText note =
                    (EditText) view.findViewById(R.id.materialEditText_premium_info_note);

            builder.negativeText(getString(R.string.alipay));
            builder.negativeColor(ContextCompat.getColor(mContext, R.color.alipay));
            builder.positiveText(getString(R.string.wechat));
            builder.positiveColor(ContextCompat.getColor(mContext, R.color.wechat));
            builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    if (!StringUtil.isMail(email.getText().toString())) {
                        Toast.makeText(mContext,
                                getString(R.string.please_input_right_mail_address),
                                Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    pay(true, amount,
                            email.getText().toString() + "\n" + note.getText().toString(), dialog);
                }
            });
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    if (AppInfoUtil.isPackageInstalled(mContext, "com.bmob.app.sport")) {
                        if (!StringUtil.isMail(email.getText().toString())) {
                            Toast.makeText(mContext,
                                    getString(R.string.please_input_right_mail_address),
                                    Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        pay(false, amount,
                                email.getText().toString() + "\n" + note.getText().toString(), dialog);
                    } else {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
                        builder.content("need install a plugin");
                        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
                                Utility.installApkFromAssets(dialog.getContext(), "BmobPayPlugin.apk");
                            }
                        });
                        builder.positiveText("install");
                        builder.negativeText("cancel");
                        builder.show();
                    }
                }
            });
            builder.autoDismiss(false);
            builder.customView(view, true);
            builder.show();
        } else {
            new SendMailAsyncTask(mContext).execute();
        }
    }

    private void pay(final boolean isAlipay, int amount, final String sendAfterPay, final MaterialDialog
            payDialog) {
        PayHelper payHelper = new PayHelper(mActivity);
        payHelper.setPayCallback(new PayHelper.PayCallback() {
            @Override
            public void onSuccess(String orderId) {
                Toast.makeText(mContext,
                        getString(R.string.pay_success),
                        Toast.LENGTH_SHORT)
                        .show();
                payDialog.dismiss();
                SendMailAsyncTask asyncTask = new SendMailAsyncTask(mContext);
                asyncTask.setStringToSend(sendAfterPay + "\norderid: " + orderId);
                asyncTask.execute();
            }

            @Override
            public void onFail() {
            }
        });
        payHelper.pay(isAlipay, amount, getString(R.string.premium_alipay_title), getString(R.string
                .premium_alipay_descript));
//
//        BP.pay(
//                mActivity,
//                getString(R.string.premium_alipay_title),
//                getString(R.string
//                        .premium_alipay_descript),
//                amount,
//                isAlipay,
//                new c.b.PListener() {
//
//                    private String mOrderid;
//
//                    @Override
//                    public void orderId(String s) {
//
//                        Toast.makeText(mActivity,
//                                getString(isAlipay ? R.string.open_alipay : R.string.open_wechat) + s,
//                                Toast.LENGTH_LONG)
//                                .show();
//                        mOrderid = s;
//                    }
//
//                    @Override
//                    public void succeed() {
//
//                    }
//
//                    @Override
//                    public void fail(int i, String s) {
//
//                    }
//
//                    @Override
//                    public void unknow() {
//                        Log.d("sip premium", "unknow");
//                    }
//                });
    }

    private class LoadAppsAsyncTask extends AsyncTask {

        private Context mContext;

        public LoadAppsAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return AppInfoUtil.getComponentInfo(mContext, true);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            List<AppInfo> appInfoList = (List<AppInfo>) o;
            setupRecyclerView(appInfoList);

            dismissIndicator();
            showRecyclerView();

            menuEnable = true;
            if (mMenu != null) {
                onCreateOptionsMenu(mMenu);
            }
        }

        private void setupRecyclerView(List<AppInfo> appInfoList) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(
                    new LinearLayoutManager(mContext,
                            LinearLayoutManager.VERTICAL,
                            false)
            );
            mAdapter = new RequestAdapter(mContext, appInfoList);
            mAdapter.setOnCheckListener(new RequestAdapter.OnCheckListener() {
                @Override
                public void OnEmpty() {
                    showFab(false);
                }

                @Override
                public void OnUnEmpty() {
                    showFab(true);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void dismissIndicator() {
        mIndicatorView.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showFab(boolean show) {
        mBinding.fabAppSelect.setShow(show);
        if (show) {
            mBinding.fabAppSelect.show();
        } else {
            mBinding.fabAppSelect.hide();
        }
    }

    private class SendMailAsyncTask extends AsyncTask<MailSenderInfo, Integer, Boolean> {
        private ProgressDialog mProgressDialog;
        private Context mContext;
        private String stringToSend = "";

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
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                AppSelectActivity.this.finish();
            } else {
                Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(MailSenderInfo... params) {
            return send(stringToSend);
        }
    }

    private void setToolbarDoubleTap(Toolbar toolbar) {
        final GestureDetector detector = new GestureDetector(this,
                new ToolbarOnGestureListener(new ToolbarOnGestureListener.DoubleTapListener() {
                    @Override
                    public void onDoubleTap() {
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                }));
        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }

    private boolean send(String sendString) {

        MailSenderInfo mailInfo;
        if (mPremium) {
            mailInfo = MailUtil.generateMailSenderInfo(
                    getStringToSend(sendString + "\n"),
                    "smtp.163.com",
                    "25",
                    true,
                    getString(R.string.feedback_mailbox),
                    getString(R.string.feedback_mail_password),
                    getString(R.string.feedback_mailbox),
                    getString(R.string.feedback_receive_mailbox_premium),
                    "premium icon request");
        } else {
            mailInfo = MailUtil.generateMailSenderInfo(
                    getStringToSend(""),
                    "smtp.163.com",
                    "25",
                    true,
                    getString(R.string.feedback_mailbox),
                    getString(R.string.feedback_mail_password),
                    getString(R.string.feedback_mailbox),
                    getString(R.string.feedback_receive_mailbox),
                    "icon request");
        }

        final boolean[] res = new boolean[1];

        MailUtil.send(mailInfo, new MailUtil.SendMailCallback() {
            @Override
            public void onSuccess() {
                res[0] = true;
            }

            @Override
            public void onFail() {
                res[0] = false;
            }
        });
        return res[0];
    }

    private String getStringToSend(String head) {
        String s = "";
        List list = mAdapter.getCheckedAppsList();
        for (int i = 0; i < list.size(); i++) {
            s += list.get(i).toString();
            s += "------------------------------\n";
        }
        return head + s;
    }

    @Override
    public void onBackPressed() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.content(getString(R.string.cancel_request));
        builder.onAny(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (which == DialogAction.POSITIVE) {
                    back();
                }
            }
        });
        builder.positiveText(getString(R.string.yes));
        builder.negativeText(getString(R.string.no));
        builder.show();
    }

    private void back() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (menuEnable) {
            getMenuInflater().inflate(R.menu.menu_app_select, menu);
        } else {
            mMenu = menu;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_just_show_without_custom) {
            item.setChecked(!item.isChecked());
            mAdapter.setShowAll(!item.isChecked());
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_premium_request) {
            if (mPremium) {
                mPremium = false;
                item.setIcon(R.drawable.ic_attach_money_white_24dp);
            } else {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
                builder.title(R.string.premium_request_title);
                builder.content(StringUtil.handleLongXmlString(getString(R.string
                        .premium_request_content)));
                builder.negativeText(R.string.cancel);
                builder.positiveText(R.string.ok);
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        if (!mLoadOk) {
                            Toast.makeText(mContext,
                                    getString(R.string.fail_open_premium),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mPremium = true;
                        item.setIcon(R.drawable.ic_money_off_white_24dp);
                    }
                });
                builder.show();
            }
        } else if (id == R.id.action_select_all) {
            mCheckAll = !mCheckAll;
            mAdapter.checkAll(mCheckAll);
            if (mCheckAll) {
                showFab(true);
            } else {
                showFab(false);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
