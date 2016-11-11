package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.utils.NetUtil;

import butterknife.OnClick;

public class FeedbackActivity extends SlideInAndOutAppCompatActivity {

    @OnClick({R.id.button_request, R.id.button_feedback_join, R.id.button_suggest})
    void onClick(View v) {
        if (!NetUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "network error", Toast.LENGTH_SHORT).show();
            return;
        }
        int id = v.getId();
        if (id == R.id.button_request) {
            Intent intent = new Intent(this, AppSelectActivity.class);
            startActivity(intent);
        } else if (id == R.id.button_suggest) {
            Intent intent = new Intent(this, FeedbackChatActivity.class);
            startActivity(intent);
        } else if (id == R.id.button_feedback_join) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/communities/115317471515103046699"));
            startActivity(intent);
        }
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init() {
        super.init();
        setToolbarBackIndicator();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }
}

