package com.sorcerer.sorcery.iconpack.ui.views;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SorceryIcons;
import com.sorcerer.sorcery.iconpack.net.leancloud.LikeBean;
import com.sorcerer.sorcery.iconpack.util.Prefs.LikePrefs;
import com.sorcerer.sorcery.iconpack.util.Prefs.Prefs;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.functions.Action1;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/2/29 0029
 */
public class LikeLayout extends FrameLayout {
    private static final String TAG = "LikeLayout";
    private LikePrefs mPrefs;
    private String mName;
    private Context mContext;
    private boolean mBind;

    private final float mTimeToScaleLikeImg = 1.5f;

    private int mFlag = 0;

    @BindView(R.id.textView_label_like)
    TextView mLikeText;
    @BindView(R.id.textView_label_dislike)
    TextView mDislikeText;

    @OnClick({R.id.textView_label_like, R.id.textView_label_dislike})
    void onClick(final View v) {
        RxPermissions.getInstance(mContext)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            return;
                        }
                        int id = v.getId();
                        if (id == R.id.textView_label_like) {
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
                    }
                });
    }


    @OnLongClick({R.id.textView_label_like, R.id.textView_label_dislike})
    boolean onLongClick(View v) {
        int id = v.getId();
        if (id == R.id.textView_label_like) {
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

    @BindViews({R.id.textView_label_like, R.id.textView_label_dislike})
    TextView[] mTextViews;

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
        mPrefs = new LikePrefs(context);

        View view = View.inflate(context, R.layout.layout_like, null);
        this.addView(view);
        ButterKnife.bind(this, view);

        for (TextView t : mTextViews) {
            t.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "like.ttf"));
        }
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
    }

    public void bindIcon(String name) {
        mBind = true;
        mName = name;
        mFlag = mPrefs.like(name).getValue();
        handleFlag(mFlag, false);
    }

    private void handleFlag(int flag, boolean scale) {
        if (flag == 0) {
            mLikeText.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
            mDislikeText.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
        } else if (flag == 1) {
            mLikeText.setTextColor(ContextCompat.getColor(mContext, R.color.pink_500));
            mDislikeText.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
            if (scale) {
                scale(mLikeText, true);
                scale(mDislikeText, false);
            }
        } else if (flag == -1) {
            mLikeText.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
            mDislikeText.setTextColor(ContextCompat.getColor(mContext, R.color.blue_grey_500));
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

    private void like(String name, boolean like) {
        String deviceId;
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        } catch (Exception e) {
            return;
        }

        LikeBean likeBean = new LikeBean();
        likeBean.setLike(like);
        likeBean.setBuild(BuildConfig.VERSION_CODE + "");
        likeBean.setDeviceId(deviceId);
        likeBean.setIconName(name);
        likeBean.saveInBackground();
    }


}