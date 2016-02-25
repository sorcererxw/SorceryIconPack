package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;

import org.w3c.dom.Text;

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
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_contributor, null);
        addView(mView);
        mAvatar = (ImageView)mView.findViewById(R.id.imageView_contributor_avatar);
        mName = (TextView)mView.findViewById(R.id.textView_contributor_name);
        mJob = (TextView)mView.findViewById(R.id.textView_contributor_job);
        mDescribe = (TextView)mView.findViewById(R.id.textView_contributor_describe);
    }

    public void setAvatar(Drawable drawable){
        mAvatar.setVisibility(VISIBLE);
        mAvatar.setImageDrawable(drawable);
    }

    public void setName(String name){
        mName.setVisibility(VISIBLE);
        mName.setText(name);
    }

    public void setJob(String job){
        mJob.setVisibility(VISIBLE);
        mJob.setText(job);
    }

    public void setDescribe(String describe){
        mDescribe.setVisibility(VISIBLE);
        mDescribe.setText(describe);
    }
}
