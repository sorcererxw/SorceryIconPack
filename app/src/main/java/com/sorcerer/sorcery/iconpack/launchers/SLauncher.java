package com.sorcerer.sorcery.iconpack.launchers;

import android.content.Context;
import android.content.Intent;

import com.sorcerer.sorcery.iconpack.R;

public class SLauncher {
    public SLauncher(Context context) {
        Intent s = new Intent("com.s.launcher.APPLY_ICON_THEME");
        s.putExtra("com.s.launcher.theme.EXTRA_PKG", context.getPackageName());
        s.putExtra("com.s.launcher.theme.EXTRA_NAME", context.getResources().getString(R.string
                .app_name));
        context.startActivity(s);

    }
}
