package com.sorcerer.sorcery.iconpack.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class ShortcutReceiver extends BroadcastReceiver {
    public ShortcutReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d(intent.getAction());
    }
}
