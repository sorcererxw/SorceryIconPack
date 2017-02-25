package com.sorcerer.sorcery.iconpack.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.test_button)
    Button mButton;

    @BindView(R.id.test_imageView)
    ImageView mImageView;

    @BindView(R.id.test_button_shortcut_change)
    Button mShortcutChangeButton;

    @BindView(R.id.test_button_shortcut_create)
    Button mShortcutCreateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        mImageView.setImageResource(R.drawable.alipay);

        mButton.setOnClickListener(v -> replace());


        mShortcutCreateButton.setOnClickListener(v -> createShortcut());
        mShortcutChangeButton.setOnClickListener(v -> changeShortcut());
    }

    private void createShortcut() {
        installShortCut(BitmapFactory
                .decodeResource(getResources(), R.drawable.oneplus_weather_alt_sunny));
    }

    private void changeShortcut() {
        installShortCut(BitmapFactory
                .decodeResource(getResources(), R.drawable.oneplus_weather_alt_cloudy));
    }

    private void installShortCut(Bitmap icon) {

        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
        // 是否可以有多个快捷方式的副本，参数如果是true就可以生成多个快捷方式，如果是false就不会重复添加
        shortcutIntent.putExtra("duplicate", true);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 要删除的应用程序的ComponentName，即应用程序包名+activity的名字
        //intent2.setComponent(new ComponentName(this.getPackageName(), this.getPackageName() + ".MainActivity"));
        mainIntent.setClass(this, this.getClass());

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, mainIntent);
        //shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
        sendBroadcast(shortcutIntent);
    }

    private void replace() {
        Bitmap androidWorkIndicator = BitmapFactory.decodeResource(getResources(),
                R.drawable.android_for_work_indicator).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.alipay)
                .copy(Bitmap.Config.ARGB_8888, true);

        androidWorkIndicator = ImageUtil.getResizedBitmap(androidWorkIndicator, 192, 192);
        bitmap = ImageUtil.getResizedBitmap(bitmap, 192, 192);

        Timber.d("height: %d, width: %d", androidWorkIndicator.getHeight(),
                androidWorkIndicator.getWidth());
        Timber.d("height: %d, width: %d", bitmap.getHeight(),
                bitmap.getWidth());
        try {
            mImageView.setImageBitmap(ImageUtil.overlay(bitmap, androidWorkIndicator));
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
