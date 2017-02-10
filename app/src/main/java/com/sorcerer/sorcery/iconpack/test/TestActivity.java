package com.sorcerer.sorcery.iconpack.test;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        mImageView.setImageResource(R.drawable.alipay);

        mButton.setOnClickListener(v -> replace());
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
            mImageView.setImageBitmap(ImageUtil.overlay(bitmap,androidWorkIndicator));
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
