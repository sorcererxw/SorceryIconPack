package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sorcerer.sorcery.iconpack.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setAction(getIntent().getAction());
        startActivity(intent);


    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0, android.R.anim.fade_out);

    }
}
