package com.sorcerer.sorcery.iconpack.feedback.request;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.AppInfo;
import com.sorcerer.sorcery.iconpack.net.avos.AvosClient;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/18
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.AppItemViewHolder> {

    private Context mContext;
    private List<CheckAppInfo> mAppInfoList;

    private static class CheckAppInfo extends AppInfo {

        private boolean mChecked = false;

        CheckAppInfo(AppInfo appInfo, Boolean check) {
            setRequestedTimes(appInfo.getRequestedTimes());
            setCode(appInfo.getCode());
            setHasCustomIcon(appInfo.isHasCustomIcon());
            setRequestedTimes(appInfo.getRequestedTimes());
            setName(appInfo.getName());
            setIcon(appInfo.getIcon());

            mChecked = check;
        }

        boolean isChecked() {
            return mChecked;
        }

        void setChecked(boolean checked) {
            mChecked = checked;
        }
    }

    private boolean mShowAll = false;

    private OnCheckListener mOnCheckListener;

    public interface OnCheckListener {
        void OnEmpty();

        void OnUnEmpty();
    }

    static class AppItemViewHolder extends RecyclerView.ViewHolder {

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

        AppItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            String t = ResourceUtil.getString(itemView.getContext(), R.string.icon_request_times);
            mPrefixTimes = t.split("#")[0];
            mSuffixTimes = t.split("#")[1];
        }

        void setTimes(int count) {
            if (count >= 0) {
                String t = mPrefixTimes + count + mSuffixTimes;
                times.setText(t);
            } else {
                times.setText("......");
            }
        }
    }

    public RequestAdapter(Context context, List<AppInfo> appInfoList) {
        mContext = context;
        mAppInfoList = new ArrayList<>();
        mAppInfoList.addAll(Stream.range(0, appInfoList.size())
                .map(i -> new CheckAppInfo(appInfoList.get(i), false))
                .collect(Collectors.toList()));
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

        holder.itemView.setOnClickListener(v -> holder.check.setChecked(!holder.check.isChecked()));
        holder.check.setOnCheckedChangeListener(null);
        holder.check.setChecked(mAppInfoList.get(realPos).isChecked());
        holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
        });
        holder.label.setText(mAppInfoList.get(realPos).getName());
        holder.icon.setImageDrawable(mAppInfoList.get(realPos).getIcon());
        if (mAppInfoList.get(realPos).getRequestedTimes() == -1) {
            holder.setTimes(-1);
            AvosClient.getInstance()
                    .getAppRequestedTime(mAppInfoList.get(realPos).getPackage())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(times -> {
                        mAppInfoList.get(realPos).setRequestedTimes(times);
                        holder.setTimes(times);
                    }, Timber::e);
        } else {
            holder.setTimes(mAppInfoList.get(realPos).getRequestedTimes());
        }
    }

    @Override
    public int getItemCount() {
        if (mShowAll) {
            return mAppInfoList.size();
        } else {
            return (int) Stream.of(mAppInfoList)
                    .filter(value -> !value.isHasCustomIcon())
                    .count();
        }
    }

    private int getCheckedCount() {
        return (int) Stream.of(mAppInfoList)
                .filter(CheckAppInfo::isChecked)
                // (hasCustomIcon && showAll) || !hasCustomIcon
                .filter(value -> !value.isHasCustomIcon() || mShowAll)
                .count();
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
        return Stream.of(mAppInfoList).filter(CheckAppInfo::isChecked).collect(Collectors.toList());
    }

    public void uncheckAfterSend() {
        Stream.of(mAppInfoList).filter(CheckAppInfo::isChecked).forEach(cai -> {
            cai.setChecked(false);
            cai.setRequestedTimes(-1);
        });
        notifyDataSetChanged();
    }

    public void checkAll(boolean check) {
        if (check) {
            Stream.of(mAppInfoList).forEach(cai -> {
                if (mShowAll) {
                    cai.setChecked(true);
                } else if (!cai.isHasCustomIcon()) {
                    cai.setChecked(true);
                }
            });
        } else {
            Stream.of(mAppInfoList).forEach(cai -> cai.setChecked(false));
        }
        notifyDataSetChanged();
    }

    public void setShowAll(boolean isShowAll) {
        mShowAll = isShowAll;
        List<CheckAppInfo> tmp = new ArrayList<>(mAppInfoList);
//        for (int i = 0; i < mAppInfoList.size(); i++) {
//            tmp.add(mAppInfoList.get(i));
//        }
        mAppInfoList.clear();
        mAppInfoList.addAll(tmp);
        notifyDataSetChanged();
        if (!isShowAll) {
            Stream.of(mAppInfoList).filter(cai -> cai.isChecked() && cai.isHasCustomIcon())
                    .forEach(cai -> cai.setChecked(false));
            if (getCheckedCount() == 0) {
                mOnCheckListener.OnEmpty();
            }
        }
    }
}
