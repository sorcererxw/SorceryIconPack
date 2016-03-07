package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sorcerer.sorcery.iconpack.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
