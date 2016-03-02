package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;

/**
 * Created by Sorcerer on 2016/2/25 0025.
 */
public class ContributorCard extends FrameLayout {
    private Context mContext;

    private ImageView mAvatar;
    private TextView mName;
    private TextView mJob;
    private TextView mDescribe;
    private View mView;
    private ImageView mWeibo;
    private ImageView mGithub;
    private ImageView mEmail;
    private ImageView mWebsite;

    public ContributorCard(Context context) {
        super(context);
        init(context);
    }

    public ContributorCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContributorCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_contributor, null);
        addView(mView);
        mAvatar = (ImageView) mView.findViewById(R.id.imageView_contributor_avatar);
        mName = (TextView) mView.findViewById(R.id.textView_contributor_name);
        mJob = (TextView) mView.findViewById(R.id.textView_contributor_job);
        mDescribe = (TextView) mView.findViewById(R.id.textView_contributor_describe);
        mWeibo = (ImageView) mView.findViewById(R.id.imageView_contributor_weibo);
        mGithub = (ImageView) mView.findViewById(R.id.imageView_contributor_github);
        mEmail = (ImageView) mView.findViewById(R.id.imageView_contributor_email);
        mWebsite = (ImageView)mView.findViewById(R.id.imageView_contributor_website);
    }

    public void setAvatar(Drawable drawable) {
        mAvatar.setVisibility(VISIBLE);
        mAvatar.setImageDrawable(drawable);
    }

    public void setAnimAvatar(Drawable drawable) {
        mAvatar.setVisibility(VISIBLE);
        mAvatar.setImageDrawable(drawable);
        ((AnimationDrawable) mAvatar.getDrawable()).start();
    }

    public void setName(String name) {
        mName.setVisibility(VISIBLE);
        mName.setText(name);
    }

    public void setJob(String job) {
        mJob.setVisibility(VISIBLE);
        mJob.setText(job);
    }

    public void setDescribe(String describe) {
        mDescribe.setVisibility(VISIBLE);
        mDescribe.setText(describe);
    }

    public void setWeibo(final Uri weibo) {
        mWeibo.setVisibility(VISIBLE);
        mWeibo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, weibo);
                mContext.startActivity(browserIntent);
            }
        });
    }

    public void setWebsite(final Uri website){
        mWebsite.setVisibility(VISIBLE);
        mWebsite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, website);
                mContext.startActivity(browserIntent);
            }
        });
    }

    public void setGithub(final Uri github) {
        mGithub.setVisibility(VISIBLE);
        mGithub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, github);
                mContext.startActivity(browserIntent);
            }
        });
    }

    public void setEmail(final String email) {
        mEmail.setVisibility(VISIBLE);
        mEmail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_SENDTO);
                    i.setData(Uri.parse("mailto:" + email));
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sorcery icon pack: suggest");
                    i.putExtra(Intent.EXTRA_TEXT, "write down your suggestion:\n");
                    mContext.startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(mContext,
                            "please login in your email app first",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}
