package com.sorcerer.sorcery.iconpack.adapters;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;

import java.util.List;

/**
 * Created by Sorcerer on 2016/1/24 0024.
 */
public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.LauncherViewHolder> {

    private List<String> mLauncherList;
    private Context mContext;
    private OnItemClickListener mListener;

    public ApplyAdapter(Context context, List launcherList) {
        mContext = context;
        mLauncherList = launcherList;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public final class LauncherViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView label;
        View main;

        public LauncherViewHolder(View itemView) {
            super(itemView);

            label = (TextView) itemView.findViewById(R.id.textView_item_apply_label);
            main = itemView;

            main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(main, getAdapterPosition());
            }
        }
    }

    @Override
    public LauncherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_apply, parent, false);
        return new LauncherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LauncherViewHolder holder, int position) {
        holder.label.setText(mLauncherList.get(position));
    }

    @Override
    public int getItemCount() {
        return mLauncherList.size();
    }

    public String getLabel(int position) {
        return mLauncherList.get(position);
    }
}
