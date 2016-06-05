package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.SorceryMenuItem;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.BaseRecyclerAdapter;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Sorcerer on 2016/3/23 0023.
 */
public class DrawerMenuAdapter extends
        BaseRecyclerAdapter<DrawerMenuAdapter.MenuViewHolder, SorceryMenuItem> {

    private static final int VIEW_TYPE_FULL = 0x0;
    private static final int VIEW_TYPE_MINI = 0x1;
    private static final int VIEW_TYPE_LINE = 0x2;

    public DrawerMenuAdapter(Context context, List<SorceryMenuItem> data) {
        super(context, data);
    }

    public class MenuViewHolder extends BaseRecyclerAdapter.BaseViewHolder {
        @BindView(R.id.imageView_drawer_menu_item_icon)
        ImageView icon;

        @BindView(R.id.textView_drawer_menu_item_label)
        TextView label;

        @BindView(R.id.linearLayout_drawer_menu_root)
        LinearLayout root;

        @BindView(R.id.linearLayout_drawer_menu_item)
        LinearLayout item;

        @BindView(R.id.linearLayout_drawer_menu_item_line)
        View line;

        public MenuViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        SorceryMenuItem item = mDataList.get(position);
        if (item.getIconRes() == 0 && item.getLabel() == null
                && item.getOnSelectListener() == null) {
            return VIEW_TYPE_LINE;
        } else if (item.getIconRes() == 0) {
            return VIEW_TYPE_MINI;
        } else {
            return VIEW_TYPE_FULL;
        }
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_drawer_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, final int position) {

        int type = getItemViewType(position);
        String label = mDataList.get(position).getLabel();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataList.get(position).getOnSelectListener() != null) {
                    mDataList.get(position).getOnSelectListener().onSelect();
                }
                Log.d(TAG, "" + getItemViewType(position));
            }
        };
        holder.icon.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        holder.label.setText(label);
        holder.root.setOnClickListener(listener);
        holder.root.setClickable(type != 2);
        holder.item.setVisibility(type == 2 ? View.GONE : View.VISIBLE);

        holder.label.setPadding(
                getItemViewType(position) == VIEW_TYPE_FULL ? DisplayUtil.dip2px(mContext, 32f)
                        : DisplayUtil.dip2px(mContext, 16f), 0, 0, 0);

        holder.icon.setImageResource(mDataList.get(position).getIconRes());

        holder.line.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
    }

    private int dp2px(int dp) {
        return DisplayUtil.dip2px(mContext, dp);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }


}
