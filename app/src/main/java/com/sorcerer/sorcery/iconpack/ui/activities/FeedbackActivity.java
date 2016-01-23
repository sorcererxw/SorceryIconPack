package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import com.sorcerer.sorcery.iconpack.util.Utility;

import java.util.List;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private Button mRequestButton;
    private Button mSuggestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

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
        if (id == R.id.button_request) {
            String s = "";
            List list = Utility.getComponentInfo(this);
            for (int i = 0; i < list.size(); i++) {
                s += list.get(i).toString();
                s += "------------------------------\n";
            }
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_mailbox)});
            i.putExtra(Intent.EXTRA_SUBJECT, "Sorcery icon pack: icon request");
            i.putExtra(Intent.EXTRA_TEXT, s);
            try {
                startActivity(Intent.createChooser(i, getString(R.string.choose_mail_app)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this,
                        "There are no email clients installed.",
                        Toast.LENGTH_SHORT).show();
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
}
