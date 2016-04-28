package com.sorcerer.sorcery.iconpack.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SIP;
import com.sorcerer.sorcery.iconpack.models.LauncherInfo;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;
import com.sorcerer.sorcery.iconpack.util.Utility;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sorcerer on 2016/1/24 0024.
 */
public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.LauncherViewHolder> {
    private static final String TAG = "ApplyAdapter";
    private List<LauncherInfo> mLauncherList;
    private Context mContext;
    private OnItemClickListener mListener;
    private ColorMatrixColorFilter mGrayScaleFilter;


    public ApplyAdapter(Context context, List<LauncherInfo> launcherList) {
        mContext = context;
        mLauncherList = launcherList;
        Collections.sort(mLauncherList, new Comparator<LauncherInfo>() {
            @Override
            public int compare(LauncherInfo lhs, LauncherInfo rhs) {
                return lhs.compareTo(rhs);
            }
        });

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        mGrayScaleFilter = new ColorMatrixColorFilter(matrix);
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
//            main.setOnTouchListener(this);
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

//        holder.icon.setImageResource(mContext.getResources()
//                .getIdentifier(mLauncherList.get(position).getLabel().replace(" ", "_")
//                                .toLowerCase(),
//                        "drawable", mContext
//                                .getPackageName()));
//
        ImageLoader.getInstance().displayImage("drawable://" + mContext.getResources()
                        .getIdentifier(mLauncherList.get(position).getLabel().replace(" ", "_")
                                        .toLowerCase(),
                                "drawable", mContext
                                        .getPackageName()),
                holder.icon, SIP.mOptions);

        setItemParams(holder, position);

        if (mLauncherList.get(position).isInstalled()) {
            holder.isInstalled.setText(mContext.getString(R.string.installed));
            holder.icon.setColorFilter(null);
            holder.label.setTextColor(ContextCompat.getColor(mContext, R.color.primary_text));
            holder.isInstalled.setTextColor(ContextCompat.getColor(mContext, R.color.primary_text));
        } else {
            holder.isInstalled.setText(mContext.getString(R.string.not_installed));
            holder.icon.setColorFilter(mGrayScaleFilter);
            holder.label.setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
            holder.isInstalled
                    .setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
        }
    }

    private void setItemParams(LauncherViewHolder holder, int position) {
        int top, bottom, left, right;

//        if (position % 2 == 0) {
//            left = 8;
//            right = 4;
//        } else {
//            right = 8;
//            left = 4;
//        }
//
//        if (position == 0 || position == 1) {
//            top = 8;
//        } else {
//            top = 4;
//        }
//
//        if (position >= getItemCount() - 2) {
//            if (getItemCount() % 2 == 0 && position == getItemCount() - 2) {
//                bottom = 8;
//            } else if (getItemCount() % 2 == 1 && position == getItemCount() - 2) {
//                bottom = 4;
//            } else {
//                bottom = 8;
//            }
//        } else {
//            bottom = 4;
//        }

        if (position % 2 == 0) {
            left = 0;
            right = 0;
        } else {
            right = 0;
            left = 0;
        }

        if (position == 0 || position == 1) {
            top = 8;
        } else {
            top = 0;
        }

        if (position >= getItemCount() - 2) {
            if (getItemCount() % 2 == 0 && position == getItemCount() - 2) {
                bottom = 8;
            } else if (getItemCount() % 2 == 1 && position == getItemCount() - 2) {
                bottom = 0;
            } else {
                bottom = 8;
            }
        } else {
            bottom = 0;
        }

        holder.main.setLayoutParams(getItemParams(left, top, right, bottom));
    }

    private LinearLayout.LayoutParams getItemParams(int left, int top, int right, int
            bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom));
        return params;
    }

    private int dp2px(int dp) {
        return DisplayUtil.dip2px(mContext, dp);
    }

    @Override
    public int getItemCount() {
        return mLauncherList.size();
    }

    public LauncherInfo getItem(int position) {
        return mLauncherList.get(position);
    }
}
