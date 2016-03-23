package com.sorcerer.sorcery.iconpack.ui.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;

import org.w3c.dom.Text;

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

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }

        setContentView(R.layout.dialog_icon_show);

        mContext = this;

        mLabel = getIntent().getStringExtra(EXTRA_LABEL);
        mName = getIntent().getStringExtra(EXTRA_NAME);
        mRes = getIntent().getIntExtra(EXTRA_RES, 0);

        if (mRes == 0) {
            this.finish();
        }

        ((TextView) findViewById(R.id.textView_dialog_title)).setText(mLabel);

        ((ImageView) findViewById(R.id.imageView_dialog_icon)).setImageResource(mRes);

        ((LikeLayout) findViewById(R.id.likeLayout)).bindIcon(mName);

        if (mLabel.equals("google plus")) {
            Button join =
                    (Button) findViewById(R.id.button_dialog_icon_join);
            join.setVisibility(View.VISIBLE);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("https://plus.google.com/communities/115317471515103046699"));
                    mContext.startActivity(intent);
                }
            });
        }


        (findViewById(R.id.relativeLayout_icon_dialog_background))
                .setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isPointInsideView(event.getX(),
                                event.getY(),
                                findViewById(R.id.cardView_icon_dialog_card))) {

                        } else {
                            onBackPressed();
                        }
                        return false;
                    }
                });
    }

    private boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        if ((x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight()))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < 21) {
            overridePendingTransition(0, android.R.anim.fade_out);
        }
    }
}
