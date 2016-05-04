package com.sorcerer.sorcery.iconpack.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.databinding.ItemDrawerMenuBinding;
import com.sorcerer.sorcery.iconpack.models.SorceryMenuItem;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;

import java.util.List;

/**
 * Created by Sorcerer on 2016/3/23 0023.
 */
public class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuAdapter.ViewHolder> {

    private static final String TAG = "DrawerMenuAdapter";

    public static class MenuItemViewModel {
        public final ObservableField<Integer> type = new ObservableField<>();
        public final ObservableField<String> label = new ObservableField<>();
        public final ObservableField<View.OnClickListener> listener = new ObservableField<>();
//        public final ObservableField<Integer> textMarginLeft = new ObservableField<>();
    }

    private static final int VIEW_TYPE_FULL = 0x0;
    private static final int VIEW_TYPE_MINI = 0x1;
    private static final int VIEW_TYPE_LINE = 0x2;

    private Context mContext;
    private List<SorceryMenuItem> mData;

    public DrawerMenuAdapter(Context context, List<SorceryMenuItem> data) {
        mContext = context;
        mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View main;
        public ImageView icon;
        public TextView label;
        public LinearLayout root;
        public LinearLayout item;
        public ItemDrawerMenuBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);

            main = itemView;
            root = binding.linearLayoutDrawerMenuRoot;
            icon = binding.imageViewDrawerMenuItemIcon;
            label = binding.textViewDrawerMenuItemLabel;
            item = binding.linearLayoutDrawerMenuItem;
        }
    }

    @Override
    public int getItemViewType(int position) {
        SorceryMenuItem item = mData.get(position);
        if (item.getIconRes() == 0 && item.getLabel() == null &&
                item.getOnSelectListener() == null) {
            return VIEW_TYPE_LINE;
        } else if (item.getIconRes() == 0) {
            return VIEW_TYPE_MINI;
        } else {
            return VIEW_TYPE_FULL;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_drawer_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MenuItemViewModel viewModel = new MenuItemViewModel();

        viewModel.type.set(getItemViewType(position));

        viewModel.label.set(mData.get(position).getLabel());
        viewModel.listener.set(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).getOnSelectListener() != null) {
                    mData.get(position).getOnSelectListener().onSelect();
                }
                Log.d(TAG, "" + getItemViewType(position));
            }
        });

        holder.binding.setItem(viewModel);

        holder.label.setPadding(getItemViewType(position) == VIEW_TYPE_FULL ?
                DisplayUtil.dip2px(mContext, 32f) : DisplayUtil.dip2px(mContext, 16f), 0, 0, 0);

        holder.icon.setImageResource(mData.get(position).getIconRes());

    }

    private int dp2px(int dp) {
        return DisplayUtil.dip2px(mContext, dp);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


}
