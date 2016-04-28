package com.sorcerer.sorcery.iconpack.ui.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.databinding.LayoutLikeBinding;
import com.sorcerer.sorcery.iconpack.util.IconBmobHelper;
import com.sorcerer.sorcery.iconpack.util.Utility;
import com.sorcerer.sorcery.iconpack.util.ViewUtil;

/**
 * Created by Sorcerer on 2016/2/29 0029.
 */
public class LikeLayout extends FrameLayout {
    private static final String TAG = "LikeLayout";
    public static final String PREF_NAME = "ICON_LIKE";
    private SharedPreferences mSharedPreferences;
    private IconBmobHelper mIconBmobHelper;
    private String mName;
    private Context mContext;
    private boolean mBind;

    private final float mTimeToScaleLikeImg = 1.5f;

    private int mFlag = 0;

    private LayoutLikeBinding mBinding;

    private TextView mLikeText;
    private TextView mDislikeText;

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
        mBinding = DataBindingUtil.bind(view);

        mLikeText = mBinding.textViewLabelLike;
        mDislikeText = mBinding.textViewLabelDislike;

        mBinding.setLikeTypeface(Typeface.createFromAsset(mContext.getAssets(), "like.ttf"));

        mLikeText.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext,
                        mContext.getString(R.string.action_like),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mDislikeText.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext,
                        mContext.getString(R.string.action_dislike),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mBinding.setLikeListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlag = 1;
                handleFlag(mFlag, true);
                if (mBind) {
                    mSharedPreferences.edit().putInt(mName, mFlag).commit();
                    mIconBmobHelper.like(mName, true);
                }
            }
        });

        mBinding.setDislikeListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlag = -1;
                handleFlag(mFlag, true);
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
        handleFlag(mFlag, false);
    }

    private void handleFlag(int flag, boolean scale) {
        if (flag == 0) {
            mBinding.setLikeColor(ContextCompat.getColor(mContext, R.color.grey_500));
            mBinding.setDislikeColor(ContextCompat.getColor(mContext, R.color.grey_500));
        } else if (flag == 1) {
            mBinding.setLikeColor(ContextCompat.getColor(mContext, R.color.pink_500));
            mBinding.setDislikeColor(ContextCompat.getColor(mContext, R.color.grey_500));
            if (scale) {
                scale(mLikeText, true);
                scale(mDislikeText, false);
            }
        } else if (flag == -1) {
            mBinding.setLikeColor(ContextCompat.getColor(mContext, R.color.grey_500));
            mBinding.setDislikeColor(ContextCompat.getColor(mContext, R.color.blue_grey_500));
            if (scale) {
                scale(mDislikeText, true);
                scale(mLikeText, false);
            }
        }
    }

    private void scale(View target, boolean bigger) {
        if (bigger) {
            target.animate().scaleX(mTimeToScaleLikeImg).scaleY(mTimeToScaleLikeImg)
                    .setDuration(100).start();
        } else {
            target.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100)
                    .start();
        }
    }

}
