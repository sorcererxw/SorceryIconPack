package com.sorcerer.sorcery.iconpack.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.views.SlideInAndOutAppCompatActivity;

public class AboutActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
