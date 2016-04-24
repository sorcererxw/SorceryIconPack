package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.databinding.ActivityFeedbackBinding;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.util.Utility;

public class FeedbackActivity extends SlideInAndOutAppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFeedbackBinding binding = DataBindingUtil.setContentView(this, R.layout
                .activity_feedback);

        setSupportActionBar(binding.toolbarFeedback.toolbarUniversal);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.setRequestListener(this);
        binding.setSuggestListener(this);
        binding.setJoinListener(this);
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
        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(this, "network error", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = v.getId();
        if (id == R.id.button_request) {
            Intent intent = new Intent(this, AppSelectActivity.class);
            startActivity(intent);
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
                Toast.makeText(this, "please login in your email app first", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (id == R.id.button_feedback_join) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                    ("https://plus.google.com/communities/115317471515103046699"));
            startActivity(intent);
        }
    }
}

