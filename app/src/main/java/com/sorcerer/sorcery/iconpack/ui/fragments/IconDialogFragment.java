package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.util.ImageUtil;
import com.sorcerer.sorcery.iconpack.util.StringUtil;
import com.sorcerer.sorcery.iconpack.util.ViewUtil;

import butterknife.BindView;

/**
 * Created by Sorcerer on 2016/7/29.
 */
public class IconDialogFragment extends BaseFragment {

    @BindView(R.id.toolbar_icon_dialog)
    Toolbar mToolbar;

    @BindView(R.id.likeLayout)
    LikeLayout mLikeLayout;

    @BindView(R.id.textView_dialog_title)
    TextView mTitleTextView;

    @BindView(R.id.imageView_dialog_icon)
    ImageView mIconImageView;

    @BindView(R.id.relativeLayout_icon_dialog_background)
    View mBackground;

    @BindView(R.id.linearLayout_dialog_icon_show)
    ViewGroup mRoot;

    @BindView(R.id.cardView_icon_dialog_card)
    CardView mCardView;


    private String mLabel;
    private String mName;
    private int mRes;
    private String mComponent;
    private String mPackageName;

    private static String mArgLabelKey = "label key";
    private static String mArgNameKey = "name key";
    private static String mArgResKey = "res key";

    public static IconDialogFragment newInstance(String label, String name, int res) {
        IconDialogFragment fragment = new IconDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(mArgLabelKey, label);
        bundle.putString(mArgNameKey, name);
        bundle.putInt(mArgResKey, res);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.fragment_icon_dialog;
    }

    @Override
    protected void init() {
        mLabel = getArguments().getString(mArgLabelKey);
        mName = getArguments().getString(mArgNameKey);
        mRes = getArguments().getInt(mArgResKey);
        mComponent = AppInfoUtil.getComponentByName(mContext, mName);
        mPackageName = StringUtil.componentInfoToPackageName(mComponent);

//        setSupportActionBar(mToolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("");
//        }

        mTitleTextView.setText(mLabel);
        mIconImageView.setImageResource(mRes);

        if (mName.contains("baidu")) {
            ImageUtil.grayScale(mIconImageView);
        }

        mLikeLayout.bindIcon(mName);

        mBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!ViewUtil.isPointInsideView(event.getX(),
                        event.getY(),
                        mCardView)) {
//                    onBackPressed();
                }
                return false;
            }
        });
    }
}
