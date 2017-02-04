package com.sorcerer.sorcery.iconpack.ui.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.utils.Prefs.LikePrefs;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/29 0029
 */
public class LikeLayout extends FrameLayout {
    private LikePrefs mPrefs;
    private String mName;
    private Context mContext;
    private boolean mBind;
    private Handler mHandler = new Handler();
    private Runnable mLikeRunnable;
    private final float mTimeToScaleLikeImg = 1.5f;

    private int mFlag = 0;

    @BindView(R.id.imageView_label_like)
    ImageView mLikeView;
    @BindView(R.id.imageView_label_dislike)
    ImageView mDislikeView;

    @OnClick({R.id.imageView_label_like, R.id.imageView_label_dislike})
    void onClick(final View v) {
        new RxPermissions((Activity) getContext())
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(grant -> {
                    if (!grant) {
                        return;
                    }
                    int id = v.getId();
                    if (id == R.id.imageView_label_like) {
                        mFlag = 1;
                        handleFlag(mFlag, true);
                        if (mBind) {
                            mPrefs.like(mName).setValue(mFlag);
                            like(mName, true);
                        }
                    } else {
                        mFlag = -1;
                        handleFlag(mFlag, true);
                        if (mBind) {
                            mPrefs.like(mName).setValue(mFlag);
                            like(mName, false);
                        }
                    }
                });
    }

    @OnLongClick({R.id.imageView_label_like, R.id.imageView_label_dislike})
    boolean onLongClick(View v) {
        int id = v.getId();
        if (id == R.id.imageView_label_like) {
            Toast.makeText(mContext,
                    mContext.getString(R.string.action_like),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(mContext,
                    mContext.getString(R.string.action_dislike),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public LikeLayout(Context context) {
        super(context);
        init();
    }

    public LikeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LikeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        mPrefs = new LikePrefs(mContext);

        View view = View.inflate(mContext, R.layout.layout_like, null);
        this.addView(view);
        ButterKnife.bind(this, this);

        mLikeView.setImageDrawable(
                new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_heart)
                        .sizeDp(128).paddingDp(12));

        mDislikeView.setImageDrawable(
                new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_heart_broken)
                        .sizeDp(128).paddingDp(12));

        mLikeView.setOnLongClickListener(v -> {
            Toast.makeText(mContext,
                    mContext.getString(R.string.action_like),
                    Toast.LENGTH_SHORT).show();
            return false;
        });

        mDislikeView.setOnLongClickListener(v -> {
            Toast.makeText(mContext,
                    mContext.getString(R.string.action_dislike),
                    Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    public void bindIcon(String name) {
        mBind = true;
        mName = name;
        mFlag = mPrefs.like(name).getValue();
        handleFlag(mFlag, false);
    }

    private void handleFlag(int flag, boolean scale) {
        if (flag == 0) {
//            mLikeText.setTextColor(ContextCompat.getColor(mContext, R.color.palette_grey_500));
//            mDislikeText.setTextColor(ContextCompat.getColor(mContext, R.color.palette_grey_500));
        } else if (flag == 1) {
//            mLikeText.setTextColor(ContextCompat.getColor(mContext, R.color.palette_pink_500));
//            mDislikeText.setTextColor(ContextCompat.getColor(mContext, R.color.palette_grey_500));
            if (scale) {
                scale(mLikeView, true);
                scale(mDislikeView, false);
            } else {
                mLikeView.setScaleX(mTimeToScaleLikeImg);
                mLikeView.setScaleY(mTimeToScaleLikeImg);
            }
        } else if (flag == -1) {
//            mLikeText.setTextColor(ContextCompat.getColor(mContext, R.color.palette_grey_500));
//            mDislikeText
//                    .setTextColor(ContextCompat.getColor(mContext, R.color.palette_blue_grey_500));
            if (scale) {
                scale(mDislikeView, true);
                scale(mLikeView, false);
            } else {
                mDislikeView.setScaleX(mTimeToScaleLikeImg);
                mDislikeView.setScaleY(mTimeToScaleLikeImg);
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

    private void like(String name, boolean like) {
        String deviceId;
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        } catch (Exception e) {
            return;
        }

        if (deviceId == null || deviceId.length() == 0) {
            return;
        }

        if (mLikeRunnable != null) {
            mHandler.removeCallbacks(mLikeRunnable);
        }

//        final LikeBean likeBean = new LikeBean();
//        likeBean.setLike(like);
//        likeBean.setBuild(BuildConfig.VERSION_CODE + "");
//        likeBean.setDeviceId(deviceId);
//        likeBean.setIconName(name);
//
//        mLikeRunnable = likeBean::saveInBackground;
//
//        mHandler.postDelayed(mLikeRunnable, 1000);
    }
}
