package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.util.Utility;

public class FeedbackActivity extends SlideInAndOutAppCompatActivity
        implements View.OnClickListener {

    private Context mContext;
    private Toolbar mToolbar;
    private Button mRequestButton;
    private Button mSuggestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar_universal);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRequestButton = (Button) findViewById(R.id.button_request);
        mSuggestButton = (Button) findViewById(R.id.button_suggest);

        mRequestButton.setOnClickListener(this);
        mSuggestButton.setOnClickListener(this);

        (findViewById(R.id.button_feedback_join)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://plus.google.com/communities/115317471515103046699"));
                mContext.startActivity(intent);
            }
        });

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
            showRequestDialog();
        } else if (id == R.id.button_suggest) {
            try {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:" + getString(R.string.feedback_receive_mailbox)));
                i.putExtra(Intent.EXTRA_SUBJECT, "Sorcery icon pack: suggest");
                i.putExtra(Intent.EXTRA_TEXT, "Version: " + BuildConfig.VERSION_NAME + "\nwrite " +
                        "down your " +
                        "suggestion:\n");
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(mContext, "please login in your email app first", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void showRequestDialog() {
        Intent intent = new Intent(this, AppSelectActivity.class);
        startActivity(intent);
    }
}

