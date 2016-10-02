package com.sorcerer.sorcery.iconpack.ui.activities.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.sorcerer.sorcery.iconpack.R;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/2
 */

public class PhotoViewActivity extends BaseActivity {
    private static final String KEY_URL = "key_url";

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

    @BindView(R.id.photoView)
    PhotoView mPhotoView;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void init() {
        String url = getIntent().getStringExtra(KEY_URL);
        Glide.with(this)
                .load(url)
                .into(mPhotoView);
    }
}
