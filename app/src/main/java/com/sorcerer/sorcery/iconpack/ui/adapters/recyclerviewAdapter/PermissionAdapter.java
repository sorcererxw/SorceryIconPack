package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.data.models.PermissionBean;
import com.sorcerer.sorcery.iconpack.utils.DisplayUtil;
import com.sorcerer.sorcery.iconpack.utils.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/12
 */

public class PermissionAdapter
        extends RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder> {

    static class PermissionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_permission_item_title)
        TextView title;

        @BindView(R.id.textView_permission_item_describe)
        TextView describe;

        PermissionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private Context mContext;

    private List<PermissionBean> mPermissionBeanList;

    public PermissionAdapter(Context context, List<PermissionBean> list) {
        mContext = context;
        mPermissionBeanList = list;
    }


    @Override
    public PermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PermissionViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_permission, parent, false));
    }

    @Override
    public void onBindViewHolder(PermissionViewHolder holder, int position) {
        if (TextUtils.isEmpty(mPermissionBeanList.get(position).getTitle())) {
            holder.title.setVisibility(View.GONE);
            holder.describe.setTextSize(16);
        } else {
            holder.title.setVisibility(View.VISIBLE);
            holder.describe.setTextSize(14);
        }
        holder.title.setText(mPermissionBeanList.get(position).getTitle());
        holder.title.setCompoundDrawablesWithIntrinsicBounds(
                mPermissionBeanList.get(position).getDrawable(), null, null, null);
        holder.title.setCompoundDrawablePadding(DisplayUtil.dip2px(mContext, 16));
        holder.describe.setText(mPermissionBeanList.get(position).getDescribe());
    }

    @Override
    public int getItemCount() {
        return mPermissionBeanList.size();
    }
}
