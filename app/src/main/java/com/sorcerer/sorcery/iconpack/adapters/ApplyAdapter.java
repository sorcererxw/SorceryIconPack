package com.sorcerer.sorcery.iconpack.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sorcerer on 2016/1/24 0024.
 */
public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.LauncherViewHolder> {

    private List<LauncherInfo> mLauncherList;
    private Context mContext;
    private OnItemClickListener mListener;

    public ApplyAdapter(Context context, List<LauncherInfo> launcherList) {
        mContext = context;
        mLauncherList = launcherList;
        Collections.sort(mLauncherList, new Comparator<LauncherInfo>() {
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

    public final class LauncherViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnTouchListener {

        TextView label;
        TextView isInstalled;
        View main;
        ImageView icon;

        public LauncherViewHolder(View itemView) {
            super(itemView);

            label = (TextView) itemView.findViewById(R.id.textView_item_apply_label);
            isInstalled = (TextView) itemView.findViewById(R.id.textView_is_installed);
            main = itemView;
            icon = (ImageView) itemView.findViewById(R.id.imageView_item_apply_icon);

            main.setOnClickListener(this);
            main.setOnTouchListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(main, getAdapterPosition());
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
        holder.label.setText(mLauncherList.get(position).getLabel());
        if (mLauncherList.get(position).isInstalled()) {
            holder.isInstalled.setText(mContext.getString(R.string.installed));
            holder.isInstalled.setTextColor(mContext.getResources().getColor(R.color.green_500));
        } else {
            holder.isInstalled.setText(mContext.getString(R.string.not_installed));
            holder.isInstalled.setTextColor(mContext.getResources().getColor(R.color.red_500));
        }
        holder.icon.setImageResource(mLauncherList.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return mLauncherList.size();
    }

    public LauncherInfo getItem(int position) {
        return mLauncherList.get(position);
    }
}
