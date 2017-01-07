package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;
import com.sorcerer.sorcery.iconpack.utils.ImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/2
 */

public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.ApplyViewHolder> {

    public interface OnApplyItemClickListener {
        void click(LauncherInfo item);
    }

    private OnApplyItemClickListener mOnApplyItemClickListener;

    private Context mContext;

    private List<LauncherInfo> mLauncherList;

    private RequestManager mGlideRequestManager;

    public ApplyAdapter(Context context,
                        List<LauncherInfo> launcherList,
                        RequestManager glideRequestManager) {
        mContext = context;
        mLauncherList = launcherList;
        mGlideRequestManager = glideRequestManager;
    }

    @Override
    public ApplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApplyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_apply, parent, false));
    }

    @Override
    public void onBindViewHolder(final ApplyViewHolder holder, int position) {
        mGlideRequestManager.load(mLauncherList.get(position).getIcon())
                .into(holder.icon);

        holder.label.setText(mLauncherList.get(position).getLabel());
        holder.itemView.setOnClickListener(view -> {
            if (mOnApplyItemClickListener != null) {
                mOnApplyItemClickListener.click(mLauncherList.get(holder.getAdapterPosition()));
            }
        });

        if (mLauncherList.get(position).isInstalled()) {
            ImageUtil.resetScale(holder.icon);
            holder.label.setTextColor(ContextCompat.getColor(mContext, R.color.primary_text));
        } else {
            ImageUtil.grayScale(holder.icon);
            holder.label.setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
        }
    }

    public void setOnApplyItemClickListener(
            OnApplyItemClickListener onApplyItemClickListener) {
        mOnApplyItemClickListener = onApplyItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mLauncherList.size();
    }

    static class ApplyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_item_apply_icon)
        ImageView icon;

        @BindView(R.id.textView_item_apply_label)
        TextView label;

        ApplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
