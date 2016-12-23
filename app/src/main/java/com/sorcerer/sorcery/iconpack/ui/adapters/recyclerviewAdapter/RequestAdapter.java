package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/18
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.AppItemViewHolder> {

    private Context mContext;
    private List<CheckAppInfo> mAppInfoList;

    static class CheckAppInfo extends AppInfo {

        private boolean mChecked = false;

        public CheckAppInfo(AppInfo appInfo, Boolean check) {
            setRequestedTimes(appInfo.getRequestedTimes());
            setCode(appInfo.getCode());
            setHasCustomIcon(appInfo.isHasCustomIcon());
            setRequestedTimes(appInfo.getRequestedTimes());
            setName(appInfo.getName());
            setIcon(appInfo.getIcon());

            mChecked = check;
        }

        public boolean isChecked() {
            return mChecked;
        }

        public void setChecked(boolean checked) {
            mChecked = checked;
        }
    }

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
            if (count >= 0) {
                String t = mPrefixTimes + count + mSuffixTimes;
                times.setText(t);
            } else {
                times.setText("......");
            }
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
        mAppInfoList = new ArrayList<>();
        for (int i = 0; i < appInfoList.size(); i++) {
            mAppInfoList.add(new CheckAppInfo(appInfoList.get(i), false));
        }
    }

    @Override
    public AppItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppItemViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_icons_request, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final AppItemViewHolder holder, int position) {
        final int realPos = getItem(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.check.setChecked(!holder.check.isChecked());
            }
        });
        holder.check.setOnCheckedChangeListener(null);
        holder.check.setChecked(mAppInfoList.get(realPos).isChecked());
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAppInfoList.get(realPos).setChecked(isChecked);
                if (isChecked) {
                    if (getCheckedCount() >= 1 && mOnCheckListener != null) {
                        mOnCheckListener.OnUnEmpty();
                    }
                } else {
                    if (getCheckedCount() == 0 && mOnCheckListener != null) {
                        mOnCheckListener.OnEmpty();
                    }
                }
            }
        });
        holder.label.setText(mAppInfoList.get(realPos).getName());
        holder.icon.setImageDrawable(mAppInfoList.get(realPos).getIcon());
        if (mAppInfoList.get(realPos).getRequestedTimes() == -1) {
            holder.setTimes(-1);
            AVQuery<AVObject> query = new AVQuery<>("RequestStatistic");
            query.whereEqualTo("package", mAppInfoList.get(realPos).getPackage());
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (list == null) {
                        return;
                    }
                    if (list.size() > 0) {
                        int t = list.get(0).getInt("count");
                        mAppInfoList.get(realPos).setRequestedTimes(t);
                        holder.setTimes(t);
                    } else {
                        mAppInfoList.get(realPos).setRequestedTimes(0);
                        holder.setTimes(0);
                    }
                }
            });
        } else {
            holder.setTimes(mAppInfoList.get(realPos).getRequestedTimes());
        }
    }

    @Override
    public int getItemCount() {
        if (mShowAll) {
            return mAppInfoList.size();
        } else {
            int cnt = 0;
            for (AppInfo appInfo : mAppInfoList) {
                if (!appInfo.isHasCustomIcon()) {
                    cnt++;
                }
            }
            return cnt;
        }
    }

    private int getCheckedCount() {
        int cnt = 0;
        for (CheckAppInfo cai : mAppInfoList) {
            if (cai.isChecked()) {
                if (cai.isHasCustomIcon() && mShowAll) {
                    cnt++;
                }
                if (!cai.isHasCustomIcon()) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    private int getItem(int pos) {
        if (mShowAll) {
            return pos;
        } else {
            int tmp = 0;
            for (int i = 0; i < mAppInfoList.size(); i++) {
                if (!mAppInfoList.get(i).isHasCustomIcon()) {
                    if (tmp == pos) {
                        return i;
                    } else {
                        tmp++;
                    }
                }
            }
        }
        return pos;
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        mOnCheckListener = onCheckListener;
    }

    public List<AppInfo> getCheckedAppsList() {
        List<AppInfo> list = new ArrayList<>();
        for (CheckAppInfo cai : mAppInfoList) {
            if (cai.isChecked()) {
                list.add(cai);
            }
        }
        return list;
    }

    public void uncheckAfterSend() {
        for (CheckAppInfo cai : mAppInfoList) {
            if (cai.isChecked()) {
                cai.setChecked(false);
                cai.setRequestedTimes(-1);
            }
        }
        notifyDataSetChanged();
    }


    public void checkAll(boolean check) {
        if (check) {
            for (CheckAppInfo cai : mAppInfoList) {
                if (mShowAll) {
                    cai.setChecked(true);
                } else if (!cai.isHasCustomIcon()) {
                    cai.setChecked(true);
                }
            }
        } else {
            for (CheckAppInfo cai : mAppInfoList) {
                cai.setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    public void setShowAll(boolean isShowAll) {
        mShowAll = isShowAll;
        List<CheckAppInfo> tmp = new ArrayList<>();
        for (int i = 0; i < mAppInfoList.size(); i++) {
            tmp.add(mAppInfoList.get(i));
        }
        mAppInfoList.clear();
        mAppInfoList.addAll(tmp);
        notifyDataSetChanged();
        if (!isShowAll) {
            for (CheckAppInfo cai : mAppInfoList) {
                if (cai.isChecked() && cai.isHasCustomIcon()) {
                    cai.setChecked(false);
                }
            }
            if (getCheckedCount() == 0) {
                mOnCheckListener.OnEmpty();
            }
        }
    }
}
