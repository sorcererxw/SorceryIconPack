package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/2/6 0006.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.AppItemViewHolder> {
    private final static String TAG = "RequestAdapter";

    private Context mContext;
    private List<AppInfo> mAppInfoList;

    private int cnt;

    private List<Boolean> mCheckedList;

    private boolean mShowAll = false;

    private OnCheckListener mOnCheckListener;

    public interface OnCheckListener {
        void OnEmpty();

        void OnUnEmpty();
    }

    public static class AppItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageVIew_icon_request_icon)
        ImageView icon;

        @BindView(R.id.textView_icon_request_label)
        TextView label;

        @BindView(R.id.textView_icon_request_times)
        TextView times;

        @BindView(R.id.checkBox_icon_request_check)
        CheckBox check;

        private static String mPrefixTimes;
        private static String mSuffixTimes;

        public AppItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            String t = ResourceUtil.getString(itemView.getContext(), R.string.icon_request_times);
            mPrefixTimes = t.split("#")[0];
            mSuffixTimes = t.split("#")[1];
        }

        public void setTimes(int count) {
            String t = mPrefixTimes + count + mSuffixTimes;
            times.setText(t);
        }

        public void show() {
            RecyclerView.LayoutParams param =
                    (RecyclerView.LayoutParams) itemView.getLayoutParams();
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
            itemView.setLayoutParams(param);
        }

        public void hide() {
            RecyclerView.LayoutParams param =
                    (RecyclerView.LayoutParams) itemView.getLayoutParams();
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
            itemView.setLayoutParams(param);
        }
    }

    public RequestAdapter(Context context, List<AppInfo> appInfoList) {
        mContext = context;
        mAppInfoList = appInfoList;
        Boolean[] checkArr = new Boolean[mAppInfoList.size()];
        Arrays.fill(checkArr, false);
        mCheckedList = Arrays.asList(checkArr);
        cnt = 0;
    }

    @Override
    public AppItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppItemViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_icon_request, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final AppItemViewHolder holder, int position) {
        if (mAppInfoList.get(position).isHasCustomIcon() && !mShowAll) {
            holder.check.setVisibility(View.GONE);
            holder.hide();
        } else {
            holder.check.setVisibility(View.VISIBLE);
            holder.show();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.check.setChecked(!holder.check.isChecked());
            }
        });
        holder.check.setOnCheckedChangeListener(null);
        holder.check.setChecked(mCheckedList.get(position));
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cnt++;
                    if (cnt == 1 && mOnCheckListener != null) {
                        mOnCheckListener.OnUnEmpty();
                    }
                } else {
                    cnt--;
                    if (cnt == 0 && mOnCheckListener != null) {
                        mOnCheckListener.OnEmpty();
                    }
                }
            }
        });
        holder.label.setText(mAppInfoList.get(position).getName());
        holder.icon.setImageDrawable(mAppInfoList.get(position).getIcon());
        if (mAppInfoList.get(position).getRequestedTimes() == -1) {
            AVQuery<AVObject> query = new AVQuery<>("RequestStatistic");
            query.whereEqualTo("package", mAppInfoList.get(position).getPackage());
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (list.size() > 0) {
                        int t = list.get(0).getInt("count");
                        mAppInfoList.get(holder.getAdapterPosition()).setRequestedTimes(t);
                        holder.setTimes(t);
                    } else {
                        mAppInfoList.get(holder.getAdapterPosition()).setRequestedTimes(0);
                        holder.setTimes(0);
                    }
                }
            });
        } else {
            holder.setTimes(mAppInfoList.get(position).getRequestedTimes());
        }
    }

    @Override
    public int getItemCount() {
        return mAppInfoList.size();
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        mOnCheckListener = onCheckListener;
    }

    public List<AppInfo> getCheckedAppsList() {
        List<AppInfo> list = new ArrayList<>();
        for (int i = 0; i < mAppInfoList.size(); i++) {
            if (mCheckedList.get(i)) {
                list.add(mAppInfoList.get(i));
            }
        }
        return list;
    }

    public void checkAll(boolean check) {
        for (int i = 0; i < mCheckedList.size(); i++) {
            if (!check) {
                mCheckedList.set(i, false);
                continue;
            }
        }
        if (check) {
            cnt = mCheckedList.size();
        } else {
            cnt = 0;
        }
        notifyDataSetChanged();
    }


    public void setShowAll(boolean isShowAll) {
        mShowAll = isShowAll;
        List<AppInfo> tmp = new ArrayList<>();
        for (int i = 0; i < mAppInfoList.size(); i++) {
            tmp.add(mAppInfoList.get(i));
        }
        mAppInfoList.clear();
        mAppInfoList.addAll(tmp);
        notifyDataSetChanged();
        if (!isShowAll) {
            for (int i = 0; i < mCheckedList.size(); i++) {
                if (mCheckedList.get(i)) {
                    if (mAppInfoList.get(i).isHasCustomIcon()) {
                        cnt--;
                        mCheckedList.set(i, false);
                        if (cnt == 0 && mOnCheckListener != null) {
                            mOnCheckListener.OnEmpty();
                        }
                    }
                }
            }
            return;
        }
        for (int i = 0; i < mCheckedList.size(); i++) {
            if (mCheckedList.get(i)) {
                if (mAppInfoList.get(i).isHasCustomIcon()) {
                    cnt++;
                    if (cnt == 1 && mOnCheckListener != null) {
                        mOnCheckListener.OnUnEmpty();
                    }
                }
            }
        }
    }
}
