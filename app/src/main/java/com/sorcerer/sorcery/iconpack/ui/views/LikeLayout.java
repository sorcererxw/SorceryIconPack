package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.utils.L;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.util.IconBmobHelper;
import com.sorcerer.sorcery.iconpack.util.PermissionsHelper;

/**
 * Created by Sorcerer on 2016/2/29 0029.
 */
public class LikeLayout extends FrameLayout {
    public static final String PREF_NAME = "ICON_LIKE";
    private SharedPreferences mSharedPreferences;
    private IconBmobHelper mIconBmobHelper;
    private String mName;
    private Context mContext;
    private boolean mBind;

    private int mFlag = 0;

    private ImageView mLikeImg;
    private ImageView mDislikeImg;
    private LinearLayout mLikeLayout;
    private LinearLayout mDislikeLayout;

    public LikeLayout(Context context) {
        super(context);
        init(context);
    }

    public LikeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LikeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_like, null);
        this.addView(view);
        mLikeImg = (ImageView) view.findViewById(R.id.imageView_icon_like);
        mDislikeImg = (ImageView) view.findViewById(R.id.imageView_icon_dislike);
        mLikeLayout = (LinearLayout) view.findViewById(R.id.linerLayout_icon_like);
        mDislikeLayout = (LinearLayout) view.findViewById(R.id.linerLayout_icon_dislike);

        mLikeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlag = 1;
                handleFlag(mFlag);
                if (mBind) {
                    mSharedPreferences.edit().putInt(mName, mFlag).commit();
                    mIconBmobHelper.like(mName, true);
                }
            }
        });

        mDislikeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlag = -1;
                handleFlag(mFlag);
                if (mBind) {
                    mSharedPreferences.edit().putInt(mName, mFlag).commit();
                    mIconBmobHelper.like(mName, false);
                }
            }
        });
    }

    public void bindIcon(String name) {
        mBind = true;
        mName = name;
        mIconBmobHelper = new IconBmobHelper(mContext);
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mFlag = mSharedPreferences.getInt(name, 0);
        handleFlag(mFlag);
    }

    private void handleFlag(int flag) {
        if (flag == 0) {

        } else if (flag == 1) {
            mDislikeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_dislike));
            mLikeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked));
        } else if (flag == -1) {
            mDislikeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_disliked));
            mLikeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
        }
    }
}
