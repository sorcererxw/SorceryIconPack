package com.sorcerer.sorcery.iconpack.ui.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sorcerer.sorcery.iconpack.R;

public class AboutDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dialog);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }
}
