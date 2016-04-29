package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.databinding.ActivityIconDialogBinding;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;
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

    private ImageView mOriginImage;

    private Activity mContext;

    private ActivityIconDialogBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_icon_show);

        mContext = this;

        mLabel = getIntent().getStringExtra(EXTRA_LABEL);
        mName = getIntent().getStringExtra(EXTRA_NAME);
        mRes = getIntent().getIntExtra(EXTRA_RES, 0);
        mComponent = AppInfoUtil.getComponentByName(mContext, mName);

        if (mRes == 0) {
            this.finish();
        }

        setSupportActionBar(mBinding.toolbarIconDialog);
        getSupportActionBar().setTitle("");

        mBinding.setTitle(mLabel);
        mBinding.setIconSrc(mRes);

        mBinding.imageViewDialogIcon.setImageResource(mRes);

        mBinding.likeLayout.bindIcon(mName);

        if (mLabel.equals("google plus")) {
            mBinding.setShowJoinButton(true);
            mBinding.setJoinListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("https://plus.google.com/communities/115317471515103046699"));
                    mContext.startActivity(intent);
                }
            });
        } else {
            mBinding.setShowJoinButton(false);
        }


        mBinding.relativeLayoutIconDialogBackground.setOnTouchListener(new View.OnTouchListener() {
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



            MenuItem showOrigin = menu.findItem(R.id.action_show_origin_icon);
            if (AppInfoUtil.isPackageInstalled(mContext,
                    StringUtil.componentInfoToPackageName(mComponent))) {
                showOrigin.setVisible(true);
            } else {
                showOrigin.setVisible(false);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_in_store) {
            final String appPackageName = StringUtil.componentInfoToPackageName(mComponent);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" +
                                appPackageName)));
            }
        } else if (id == R.id.action_show_origin_icon) {
            try {
                if (mOriginImage == null) {
                    mOriginImage = new ImageView(mContext);
                    mOriginImage.setImageDrawable(getPackageManager().getApplicationIcon(StringUtil
                            .componentInfoToPackageName(mComponent)));
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            (int) mContext.getResources().getDimension(R.dimen.dialog_icon_size),
                            (int) mContext.getResources().getDimension(R.dimen.dialog_icon_size)
                    );
                    mOriginImage.setLayoutParams(params);
                    mOriginImage.setPadding(DisplayUtil.dip2px(mContext, 8), 0, 0, 0);
                }
                if (mBinding.linearLayoutDialogIconShow.getChildCount() > 1) {
                    mBinding.linearLayoutDialogIconShow.removeView(mOriginImage);
                } else {
                    mBinding.linearLayoutDialogIconShow.addView(mOriginImage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
