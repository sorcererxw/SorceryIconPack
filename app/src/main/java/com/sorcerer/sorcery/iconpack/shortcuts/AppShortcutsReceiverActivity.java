package com.sorcerer.sorcery.iconpack.shortcuts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sorcerer.sorcery.iconpack.apply.database.pixel.PixelApplySingleInstanceProgressActivity;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/9
 */

public class AppShortcutsReceiverActivity extends AppCompatActivity {
    public static final String ACTION_APPLY_PIXEL = "action_apply_pixel";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_APPLY_PIXEL:
                    PixelApplySingleInstanceProgressActivity.apply(this, true);
                    break;
            }
        }
        finish();
    }
}
