package com.sorcerer.sorcery.iconpack.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.SorceryMenuItem;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;
import com.sorcerer.sorcery.iconpack.util.Utility;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Sorcerer on 2016/3/23 0023.
 */
public class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuAdapter.ViewHolder> {

    private Context mContext;
    private List<SorceryMenuItem> mData;
    private View mHelp;

    public DrawerMenuAdapter(Context context, List<SorceryMenuItem> data) {
        mContext = context;
        mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View main;
        public ImageView icon;
        public TextView label;
        public LinearLayout root;

        public ViewHolder(View itemView) {
            super(itemView);

            main = itemView;
            root = (LinearLayout) itemView.findViewById(R.id.linearLayout_drawer_menu_root);
            icon = (ImageView) itemView.findViewById(R.id.imageView_drawer_menu_item_icon);
            label = (TextView) itemView.findViewById(R.id.textView_drawer_menu_item_label);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_drawer_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == 4) {
            mHelp = holder.main;
        }

        holder.label.setText(mData.get(position).getLabel());
        holder.icon.setImageResource(mData.get(position).getIconRes());
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).getOnSelectListener() != null) {
                    mData.get(position).getOnSelectListener().onSelect();
                }
            }
        });

        if (position == 0) {
            holder.root.setPadding(0, dp2px(12), 0, dp2px(12));
        } else {
            holder.root.setPadding(0, dp2px(12), 0, dp2px(12));
        }
    }

    private int dp2px(int dp) {
        return DisplayUtil.dip2px(mContext, dp);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public View getHelp() {
        return mHelp;
    }
}
