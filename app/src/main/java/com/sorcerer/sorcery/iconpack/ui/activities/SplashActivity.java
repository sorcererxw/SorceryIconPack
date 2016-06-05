package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Toast.makeText(this, "splash", Toast.LENGTH_SHORT).show();

        findViewById(android.R.id.content).postDelayed(new Runnable() {
            @Override
            public void run() {
                jump();
            }
        }, 2000);


    }

    private void jump() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(getIntent().getAction());
        startActivity(intent);

        finish();
    }
}
