package com.sorcerer.sorcery.iconpack.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/24 0024
 */
public class LauncherApplier {
    private static final String TAG = LauncherApplier.class.getSimpleName();

    public static final int TYPE_ACTION = 0x0;
    public static final int TYPE_ADWEX = 0x1;
    public static final int TYPE_ADW = 0x2;
    public static final int TYPE_APEX = 0x3;
    public static final int TYPE_ATOM = 0x4;
    public static final int TYPE_AVIATE = 0x5;
    public static final int TYPE_CMTHEMEENGINE = 0x6;
    public static final int TYPE_EPIC = 0x7;
    public static final int TYPE_GO = 0x8;
    public static final int TYPE_HOLOHD = 0x9;
    public static final int TYPE_HOLO = 0xa;
    public static final int TYPE_INSPIRE = 0xb;
    public static final int TYPE_KK = 0xc;
    public static final int TYPE_LGHOME = 0xe;
    public static final int TYPE_L = 0xf;
    public static final int TYPE_LUCID = 0x10;
    public static final int TYPE_MINI = 0x11;
    public static final int TYPE_NEMUS = 0x12;
    public static final int TYPE_NEXT = 0x13;
    public static final int TYPE_NINE = 0x14;
    public static final int TYPE_NOVA = 0x15;
    public static final int TYPE_S = 0x16;
    public static final int TYPE_SMART = 0x17;
    public static final int TYPE_SMARTPRO = 0x18;
    public static final int TYPE_SOLO = 0x19;
    public static final int TYPE_TSF = 0x1a;

    public static void applyLauncher(Context context, String label) {
        label = label.toLowerCase();
//        Log.d(TAG, label);
        switch (label) {
            case "action":
                applyLauncher(context, TYPE_ACTION);
                break;
            case "adwex":
                applyLauncher(context, TYPE_ADWEX);
                break;
            case "apex":
                applyLauncher(context, TYPE_APEX);
                break;
            case "adw":
//                switch (label.toLowerCase().split(" ")[1]) {
//                    case "ex":
//                        applyLauncher(context, TYPE_ADWEX);
//                        break;
//                    default:
                applyLauncher(context, TYPE_ADW);
//                }
                break;
            case "atom":
                applyLauncher(context, TYPE_ATOM);
                break;
            case "aviate":
                applyLauncher(context, TYPE_AVIATE);
                break;
            case "cm":
                applyLauncher(context, TYPE_CMTHEMEENGINE);
                break;
            case "epic":
                applyLauncher(context, TYPE_EPIC);
                break;
            case "go":
                applyLauncher(context, TYPE_GO);
                break;
            case "holohd":
                applyLauncher(context, TYPE_HOLOHD);
                break;
            case "holo":
                applyLauncher(context, TYPE_HOLO);
                break;
            case "inspire":
                applyLauncher(context, TYPE_INSPIRE);
                break;
            case "kk":
                applyLauncher(context, TYPE_KK);
                break;
            case "lghome":
                applyLauncher(context, TYPE_LGHOME);
                break;
            case "l":
                applyLauncher(context, TYPE_L);
                break;
            case "lucid":
                applyLauncher(context, TYPE_LUCID);
                break;
            case "mini":
                applyLauncher(context, TYPE_MINI);
                break;
            case "nemus":
                applyLauncher(context, TYPE_NEMUS);
                break;
            case "next":
                applyLauncher(context, TYPE_NEXT);
                break;
            case "nova":
                applyLauncher(context, TYPE_NOVA);
                break;
            case "nine":
                applyLauncher(context, TYPE_NINE);
                break;
            case "s":
                applyLauncher(context, TYPE_S);
                break;
            case "smart":
                applyLauncher(context, TYPE_SMART);
                break;
            case "smartpro":
                applyLauncher(context, TYPE_SMARTPRO);
                break;
            case "solo":
                applyLauncher(context, TYPE_SOLO);
                break;
            case "tsf":
                applyLauncher(context, TYPE_TSF);
                break;
            default:
                Log.d("<---ERROE--->", "no such launcher");
        }
    }

    public static void applyLauncher(Context context, int launcherType) {
        switch (launcherType) {
            case TYPE_ACTION:
                actionLauncher(context);
                break;
            case TYPE_ADWEX:
                adwexLauncher(context);
                break;
            case TYPE_ADW:
                adwLauncher(context);
                break;
            case TYPE_APEX:
                apexLauncher(context);
                break;
            case TYPE_ATOM:
                atomLauncher(context);
                break;
            case TYPE_AVIATE:
                aviateLauncher(context);
                break;
            case TYPE_CMTHEMEENGINE:
                cmthemeengineLauncher(context);
                break;
            case TYPE_EPIC:
                epicLauncher(context);
                break;
            case TYPE_GO:
                goLauncher(context);
                break;
            case TYPE_HOLOHD:
                holohdLauncher(context);
                break;
            case TYPE_HOLO:
                holoLauncher(context);
                break;
            case TYPE_INSPIRE:
                inspireLauncher(context);
                break;
            case TYPE_KK:
                kkLauncher(context);
                break;
            case TYPE_LGHOME:
                lghomeLauncher(context);
                break;
            case TYPE_L:
                lLauncher(context);
                break;
            case TYPE_LUCID:
                lucidLauncher(context);
                break;
            case TYPE_MINI:
                miniLauncher(context);
                break;
            case TYPE_NEMUS:
                nemusLauncher(context);
                break;
            case TYPE_NEXT:
                nextLauncher(context);
                break;
            case TYPE_NINE:
                nineLauncher(context);
                break;
            case TYPE_NOVA:
                novaLauncher(context);
                break;
            case TYPE_S:
                sLauncher(context);
                break;
            case TYPE_SMART:
                smartLauncher(context);
                break;
            case TYPE_SMARTPRO:
                smartproLauncher(context);
                break;
            case TYPE_SOLO:
                soloLauncher(context);
                break;
            case TYPE_TSF:
                tsfLauncher(context);
                break;
        }
    }

    public static void actionLauncher(Context context) {
        Intent action = context.getPackageManager()
                .getLaunchIntentForPackage("com.actionlauncher.playstore");
        action.putExtra("apply_icon_pack", context.getPackageName());
        context.startActivity(action);
    }

    public static void adwexLauncher(Context context) {
        Intent intent = new Intent("org.adwfreak.launcher.SET_THEME");
        intent.putExtra("org.adwfreak.launcher.theme.NAME", context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void adwLauncher(Context context) {
        Intent intent = new Intent("org.adw.launcher.SET_THEME");
        intent.putExtra("org.adw.launcher.theme.NAME", context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void apexLauncher(Context context) {
        Intent intent = new Intent("com.anddoes.launcher.SET_THEME");
        intent.putExtra("com.anddoes.launcher.THEME_PACKAGE_NAME", context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void atomLauncher(Context context) {
        Intent atom = new Intent("com.dlto.atom.launcher.intent.action.ACTION_VIEW_THEME_SETTINGS");
        atom.setPackage("com.dlto.atom.launcher");
        atom.putExtra("packageName", context.getPackageName());
        atom.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(atom);
    }

    public static void aviateLauncher(Context context) {
        Intent aviate = new Intent("com.tul.aviate.SET_THEME");
        aviate.setPackage("com.tul.aviate");
        aviate.putExtra("THEME_PACKAGE", context.getPackageName());
        aviate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(aviate);
    }


    public static void cmthemeengineLauncher(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("org.cyanogenmod.theme.chooser",
                "org.cyanogenmod.theme.chooser.ChooserActivity"));
        intent.putExtra("pkgName", context.getPackageName());
        context.startActivity(intent);
    }

    public static void epicLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.epic.launcher", "com.epic.launcher.s"));
        context.startActivity(intent);
    }

    public static void goLauncher(Context context) {
        Intent intent =
                context.getPackageManager().getLaunchIntentForPackage("com.gau.go.launcherex");
        Intent go = new Intent("com.gau.go.launcherex.MyThemes.mythemeaction");
        go.putExtra("type", 1);
        go.putExtra("pkgname", context.getPackageName());
        context.sendBroadcast(go);
        context.startActivity(intent);
    }

    public static void holohdLauncher(Context context) {
        Intent holohdApply = new Intent(Intent.ACTION_MAIN);
        holohdApply.setComponent(new ComponentName("com.mobint.hololauncher.hd",
                "com.mobint.hololauncher.SettingsActivity"));
        context.startActivity(holohdApply);
    }

    public static void holoLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.mobint.hololauncher",
                "com.mobint.hololauncher.SettingsActivity"));
        context.startActivity(intent);
    }

    public static void inspireLauncher(Context context) {
        Intent inspireMain = context.getPackageManager()
                .getLaunchIntentForPackage("com.bam.android.inspirelauncher");
        Intent inspire = new Intent("com.bam.android.inspirelauncher.action.ACTION_SET_THEME");
        inspire.putExtra("icon_pack_name", context.getPackageName());
        context.sendBroadcast(inspire);
        context.startActivity(inspireMain);
    }

    public static void kkLauncher(Context context) {
        Intent kkApply = new Intent("com.kk.launcher.APPLY_ICON_THEME");
        kkApply.putExtra("com.kk.launcher.theme.EXTRA_PKG", context.getPackageName());
        kkApply.putExtra("com.kk.launcher.theme.EXTRA_NAME", context.getResources().getString(R
                .string.app_name));
        context.startActivity(kkApply);
    }

    public static void lghomeLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.lge.launcher2",
                "com.lge.launcher2.homesettings.HomeSettingsPrefActivity"));
        context.startActivity(intent);
    }

    public static void lLauncher(Context context) {
        Intent l = new Intent("com.l.launcher.APPLY_ICON_THEME", null);
        l.putExtra("com.l.launcher.theme.EXTRA_PKG", context.getPackageName());
        context.startActivity(l);
    }

    public static void lucidLauncher(Context context) {
        Intent lucidApply = new Intent("com.powerpoint45.action.APPLY_THEME", null);
        lucidApply.putExtra("icontheme", context.getPackageName());
        context.startActivity(lucidApply);
    }

    public static void miniLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.jiubang.go.mini.launcher",
                "com.jiubang.go.mini.launcher.setting.MiniLauncherSettingActivity"));
        context.startActivity(intent);
    }

    public static void nemusLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.nemustech.launcher",
                "com.nemustech.spareparts.SettingMainActivity"));
        context.startActivity(intent);
    }

    public static void nextLauncher(Context context) {
        Intent nextApply =
                context.getPackageManager().getLaunchIntentForPackage("com.gtp.nextlauncher");
        if (nextApply == null) {
            nextApply = context.getPackageManager()
                    .getLaunchIntentForPackage("com.gtp.nextlauncher.trial");
        }
        Intent next = new Intent("com.gau.go.launcherex.MyThemes.mythemeaction");
        next.putExtra("type", 1);
        next.putExtra("pkgname", context.getPackageName());
        context.sendBroadcast(next);
        context.startActivity(nextApply);
    }

    public static void nineLauncher(Context context) {
        Intent nineApply =
                context.getPackageManager().getLaunchIntentForPackage("com.gidappsinc.launcher");
        Intent nine = new Intent("com.gridappsinc.launcher.action.THEME");
        try {
            int NineLauncherVersion = context.getPackageManager()
                    .getPackageInfo("com.gidappsinc.launcher", 0).versionCode;
            if (NineLauncherVersion >= 12210) {
                nine.putExtra("iconpkg", context.getPackageName());
                nine.putExtra("launch", true);
                context.sendBroadcast(nine);
            } else {
                Toast.makeText(context, "Update your nine launcher", Toast.LENGTH_SHORT).show();
            }
            context.startActivity(nineApply);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }

    public static void novaLauncher(Context context) {
        Intent intent = new Intent("com.teslacoilsw.launcher.APPLY_ICON_THEME");
        intent.setPackage("com.teslacoilsw.launcher");
        intent.putExtra("com.teslacoilsw.launcher.extra.ICON_THEME_TYPE", "GO");
        intent.putExtra("com.teslacoilsw.launcher.extra.ICON_THEME_PACKAGE",
                context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void sLauncher(Context context) {
        Intent s = new Intent("com.s.launcher.APPLY_ICON_THEME");
        s.putExtra("com.s.launcher.theme.EXTRA_PKG", context.getPackageName());
        s.putExtra("com.s.launcher.theme.EXTRA_NAME", context.getResources().getString(R.string
                .app_name));
        context.startActivity(s);
    }

    public static void smartLauncher(Context context) {
        Intent smartlauncherIntent = new Intent("ginlemon.smartlauncher.setGSLTHEME");
        smartlauncherIntent.putExtra("package", context.getPackageName());
        context.startActivity(smartlauncherIntent);
    }

    public static void smartproLauncher(Context context) {
        Intent smartlauncherIntent = new Intent("ginlemon.smartlauncher.setGSLTHEME");
        smartlauncherIntent.putExtra("package", context.getPackageName());
        context.startActivity(smartlauncherIntent);
    }

    public static void soloLauncher(Context context) {
        Intent soloApply =
                context.getPackageManager().getLaunchIntentForPackage("home.solo.launcher.free");
        Intent solo = new Intent("home.solo.launcher.free.APPLY_THEME");
        solo.putExtra("EXTRA_PACKAGENAME", context.getPackageName());
        solo.putExtra("EXTRA_THEMENAME", context.getString(R.string.app_name));
        context.sendBroadcast(solo);
        context.startActivity(soloApply);
    }

    public static void tsfLauncher(Context context) {
        Intent tsfApply = context.getPackageManager().getLaunchIntentForPackage("com.tsf.shell");
        Intent tsf = new Intent("android.intent.action.MAIN");
        tsf.setComponent(new ComponentName("com.tsf.shell", "com.tsf.shell.ShellActivity"));
        context.sendBroadcast(tsf);
        context.startActivity(tsfApply);
    }

    public void Layers(Context context) {
        try {
            Intent layers = new Intent("android.intent.action.MAIN");
            layers.setComponent(new ComponentName("com.lovejoy777.rroandlayersmanager",
                    "com.lovejoy777.rroandlayersmanager.menu"));
            layers.putExtra("pkgName", context.getPackageName());
            context.startActivity(layers);
        } catch (Exception e) {
            Intent layers = new Intent("android.intent.action.MAIN");
            layers.setComponent(new ComponentName("com.lovejoy777.rroandlayersmanager",
                    "com.lovejoy777.rroandlayersmanager.MainActivity"));
            layers.putExtra("pkgName", context.getPackageName());
            context.startActivity(layers);
        }
    }

    private void LgHomeLauncher(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.lge.launcher2",
                "com.lge.launcher2.homesettings.HomeSettingsPrefActivity"));
        context.startActivity(intent);
    }

}
