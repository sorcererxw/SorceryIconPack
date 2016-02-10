package com.sorcerer.sorcery.iconpack.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.AppInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sorcerer on 2016/2/6 0006.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.AppItemViewHolder> {
    private int cnt;
    private Context mContext;
    private List<AppInfo> mAppInfoList;
    private List<Boolean> mCheckedList;

    private OnCheckListener mOnCheckListener;

    public interface OnCheckListener {
        void OnEmpty();

        void OnUnEmpty();
    }

    public class AppItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView label;
        public CheckBox check;
        public View main;

        public AppItemViewHolder(View itemView) {
            super(itemView);
            main = itemView;
            icon = (ImageView) itemView.findViewById(R.id.imageVIew_icon_request_icon);
            label = (TextView) itemView.findViewById(R.id.textView_icon_request_label);
            check = (CheckBox) itemView.findViewById(R.id.checkBox_icon_request_check);
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
    public void onBindViewHolder(final AppItemViewHolder holder, final int position) {
        holder.main.setOnClickListener(new View.OnClickListener() {
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
                mCheckedList.set(position, isChecked);
                Log.d("sip", mCheckedList.toString());
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
            mCheckedList.set(i, check);
        }
        if (check == true) {
            cnt = mCheckedList.size();
        } else {
            cnt = 0;
        }
        notifyDataSetChanged();
    }
}
