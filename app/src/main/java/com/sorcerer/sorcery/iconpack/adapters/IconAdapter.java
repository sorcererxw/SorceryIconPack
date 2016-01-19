package com.sorcerer.sorcery.iconpack.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sorcerer on 2016/1/19 0019.
 */
public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconItemViewHolder> {

    private String[] mIconNames;
    private Context mContext;
    private List<Integer> mItems;

    public final static class IconItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public View main;

        public IconItemViewHolder(View itemView) {
            super(itemView);

            main = itemView;
            icon = (ImageView) main.findViewById(R.id.imageView_item_icon_grid);
        }
    }

    public IconAdapter(Context context) {
        mContext = context;
        loadIcons();
    }

    private void loadIcons() {
        mItems = new ArrayList<>();
        final String packageName = mContext.getPackageName();
        addIcons(mContext.getResources(), packageName);
    }

    private void addIcons(Resources resources, String packageName) {
        mIconNames = resources.getStringArray(R.array.icon_pack);
        for (String name : mIconNames) {
            int res = resources.getIdentifier(name, "drawable", packageName);
            if (res != 0) {
                final int thumbRes = resources.getIdentifier(name, "drawable", packageName);
                if (thumbRes != 0) {
                    mItems.add(thumbRes);
                }
            }
        }
    }

    public IconItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_icon_grid, parent, false);
        return new IconItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IconItemViewHolder holder, final int position) {
        holder.icon.setImageResource(mItems.get(position));
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View iconDialog = View.inflate(mContext, R.layout.dialog_icon_show, null);
                ((ImageView) iconDialog.findViewById(R.id.imageView_dialog_icon))
                        .setImageResource(mItems.get(position));
                ((TextView) iconDialog.findViewById(R.id.textView_dialog_icon_label))
                        .setText(mIconNames[position].toLowerCase(Locale.getDefault()));
                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(iconDialog);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
