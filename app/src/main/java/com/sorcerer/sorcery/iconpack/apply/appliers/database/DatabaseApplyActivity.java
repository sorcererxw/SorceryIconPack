package com.sorcerer.sorcery.iconpack.apply.appliers.database;

import android.support.v7.app.AppCompatActivity;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/10
 */

public abstract class DatabaseApplyActivity extends AppCompatActivity {
    protected abstract void toLauncher();

    protected abstract String provideLauncherPackage();

    protected abstract String provideLauncherComponent();
}
