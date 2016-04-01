package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.a.a.a.V;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SIP;
import com.sorcerer.sorcery.iconpack.adapters.RequestAdapter;
import com.sorcerer.sorcery.iconpack.adapters.ViewPageAdapter;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.MailSenderInfo;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.ui.views.MyFloatingActionButton;
import com.sorcerer.sorcery.iconpack.ui.views.ScrollDetectRecyclerView;
import com.sorcerer.sorcery.iconpack.util.SimpleMailSender;
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
    private MyFloatingActionButton mFab;
    private boolean mCheckAll = false;
    private boolean menuEnable;
    private Menu mMenu;
    private boolean mPremium = false;
    private Activity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_universal);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);


        setToolbarDoubleTap(toolbar);

        BP.init(mContext, getString(R.string.bmob_app_id));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_app_select);

        mIndicatorView = (AVLoadingIndicatorView) findViewById(R.id
                .avLoadingIndicatorView_icon_select);

        mFab = (MyFloatingActionButton) findViewById(R.id.fab_app_select);
        assert mFab != null;
        mFab.setOnClickListener(this);

        menuEnable = false;
        new LoadAppsAsyncTask(this).execute();
    }

    @Override
    public void onClick(View v) {
        if (mPremium) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
            builder.title("confirm your order");
            String s = "";
            final List<String> list = mAdapter.getSelectedAppsNameList();
            for (int i = 0; i < list.size(); i++) {
                s += list.get(i) + "\n";
            }
            builder.content(s + "\n" + list.size() * 2 + " yuan");
            builder.positiveText("ok");
            builder.negativeText("cancel");
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    pay(true, list.size() * 2);
                }
            });
            builder.show();
        } else {
            send();
        }
    }

    private void pay(boolean isAlipay, int amount) {
        BP.pay(mActivity, "premium", "thx", amount, isAlipay, new c.b.PListener() {

            @Override
            public void orderId(String s) {
                Toast.makeText(mActivity, getString(R.string.open_alipay), Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void succeed() {
                send();
            }

            @Override
            public void fail(int i, String s) {
            }

            @Override
            public void unknow() {
                Log.d("sip donate", "unknow");
            }
        });
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
            return Utility.getComponentInfo(mContext, true);
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
                    hideFab();
                }

                @Override
                public void OnUnEmpty() {
                    showFab();
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

    private void showFab() {
        mFab.setShow(true);
        mFab.show();
    }

    private void hideFab() {
        mFab.setShow(false);
        mFab.hide();
    }

    private class SendMailAsyncTask extends AsyncTask<MailSenderInfo, Integer, Boolean> {
        private ProgressDialog mProgressDialog;
        private Context mContext;

        public SendMailAsyncTask(Context context) {
            mContext = context;
            mProgressDialog = new ProgressDialog(context);
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
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                AppSelectActivity.this.finish();
            } else {
                Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(MailSenderInfo... params) {
            String s = "";
            List list = mAdapter.getCheckedAppsList();
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
                e.printStackTrace();
                return false;
            }
        }
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
                builder.content(Utility.handleLongXmlString(getString(R.string
                        .premium_request_content)));
                builder.negativeText(R.string.cancel);
                builder.positiveText(R.string.ok);
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
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
                showFab();
            } else {
                hideFab();
            }
        }

        return super.onOptionsItemSelected(item);
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

    private void send() {
        try {
            MailSenderInfo mailInfo = new MailSenderInfo();
            mailInfo.setMailServerHost("smtp.163.com");
            mailInfo.setMailServerPort("25");

            mailInfo.setValidate(true);
            mailInfo.setUserName(getString(R.string.feedback_mailbox));
            mailInfo.setPassword(getString(R.string.feedback_mail_password));
            mailInfo.setFromAddress(getString(R.string.feedback_mailbox));
            if (mPremium) {
                mailInfo.setToAddress(getString(R.string.feedback_receive_mailbox_premium));
                mailInfo.setSubject("premium icon request");
            } else {
                mailInfo.setToAddress(getString(R.string.feedback_receive_mailbox));
                mailInfo.setSubject("icon request");
            }
            SendMailAsyncTask myAsyncTask = new SendMailAsyncTask(mContext);
            myAsyncTask.execute(mailInfo);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

}
