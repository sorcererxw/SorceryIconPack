package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.databinding.ActivityIconDialogBinding;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.util.StringUtil;
import com.sorcerer.sorcery.iconpack.util.ViewUtil;

/**
 * Created by Sorcerer on 2016/3/22 0022.
 */

public class IconDialogActivity extends AppCompatActivity {

    public static final String EXTRA_RES = "EXTRA_RES";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_LABEL = "EXTRA_LABEL";

    private String mLabel;
    private String mName;
    private int mRes;
    private String mComponent;

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }

        ActivityIconDialogBinding binding =
                DataBindingUtil.setContentView(this, R.layout.dialog_icon_show);

        mContext = this;

        mLabel = getIntent().getStringExtra(EXTRA_LABEL);
        mName = getIntent().getStringExtra(EXTRA_NAME);
        mRes = getIntent().getIntExtra(EXTRA_RES, 0);
        mComponent = AppInfoUtil.getComponentByName(mContext, mName);

        if (mRes == 0) {
            this.finish();
        }

        setSupportActionBar(binding.toolbarIconDialog);
        getSupportActionBar().setTitle("");

        binding.setTitle(mLabel);
        binding.setIconSrc(mRes);

        binding.imageViewDialogIcon.setImageResource(mRes);

        binding.likeLayout.bindIcon(mName);

        if (mLabel.equals("google plus")) {
            binding.setShowJoinButton(true);
            binding.setJoinListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("https://plus.google.com/communities/115317471515103046699"));
                    mContext.startActivity(intent);
                }
            });
        } else {
            binding.setShowJoinButton(false);
        }


        binding.relativeLayoutIconDialogBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (ViewUtil.isPointInsideView(event.getX(),
                        event.getY(),
                        findViewById(R.id.cardView_icon_dialog_card))) {

                } else {
                    onBackPressed();
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < 21) {
            overridePendingTransition(0, android.R.anim.fade_out);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mComponent != null) {
            getMenuInflater().inflate(R.menu.menu_icon_dialog, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_in_store) {
            final String appPackageName = StringUtil.componentInfoToPackageName(mComponent);
//            Toast.makeText(mContext, appPackageName, Toast.LENGTH_SHORT).show();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" +
                                appPackageName)));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
