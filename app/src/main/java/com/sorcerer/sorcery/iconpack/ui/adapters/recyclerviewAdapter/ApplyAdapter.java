package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.BaseRecyclerAdapter;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.GridRecyclerAdapter;
import com.sorcerer.sorcery.iconpack.utils.ImageUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/24 0024
 */
public class ApplyAdapter
        extends GridRecyclerAdapter<ApplyAdapter.LauncherViewHolder, LauncherInfo> {
    private OnItemClickListener mListener;

    public ApplyAdapter(Context context, List<LauncherInfo> launcherList, int span) {
        super(context, launcherList, span);
        Collections.sort(mDataList, new Comparator<LauncherInfo>() {
            @Override
            public int compare(LauncherInfo lhs, LauncherInfo rhs) {
                return lhs.compareTo(rhs);
            }
        });
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public final class LauncherViewHolder extends BaseRecyclerAdapter.BaseViewHolder implements
            View.OnClickListener, View.OnTouchListener {

        @BindView(R.id.cardView_apply_item)
        CardView card;

        @BindView(R.id.textView_item_apply_label)
        TextView label;

        @BindView(R.id.textView_is_installed)
        TextView isInstalled;

        @BindView(R.id.imageView_item_apply_icon)
        ImageView icon;

        public LauncherViewHolder(View itemView) {
            super(itemView);

            card.setOnClickListener(this);
            card.setOnTouchListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(card, getAdapterPosition());
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.animate().translationZ(5).start();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.animate().translationZ(0).start();
                    }
                    break;
            }
            return false;
        }
    }

    @Override
    public LauncherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_apply, parent, false);
        return new LauncherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LauncherViewHolder holder, int position) {
        holder.label.setText(mDataList.get(position).getLabel());

        Glide.with(mContext)
                .load(mDataList.get(position).getIcon())
                .into(holder.icon);

        setViewMargin(holder.card, position, 2, holder);

        if (mDataList.get(position).isInstalled()) {
            holder.isInstalled.setText(mContext.getString(R.string.installed));
            ImageUtil.resetScale(holder.icon);
            holder.label.setTextColor(ContextCompat.getColor(mContext, R.color.primary_text));
            holder.isInstalled.setTextColor(ContextCompat.getColor(mContext, R.color.primary_text));
        } else {
            holder.isInstalled.setText(mContext.getString(R.string.not_installed));
            ImageUtil.grayScale(holder.icon);
            holder.label.setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
            holder.isInstalled
                    .setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
        }
    }

    public LauncherInfo getItem(int position) {
        return mDataList.get(position);
    }
}
