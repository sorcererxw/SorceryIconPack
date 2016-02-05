package com.sorcerer.sorcery.iconpack.ui.views;

import android.support.v7.app.AppCompatActivity;

import com.sorcerer.sorcery.iconpack.R;

/**
 * Created by Sorcerer on 2016/2/5 0005.
 */
public class SlideInAndOutAppCompatActivity extends AppCompatActivity{
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_right_out);
    }
}
