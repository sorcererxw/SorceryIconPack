package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.fragments.LazyIconFragment;
import com.sorcerer.sorcery.iconpack.utils.Prefs.SorceryPrefs;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/20
 */

public class CustomizeTabsAdapter extends RecyclerView.Adapter<CustomizeTabsAdapter.TabViewHolder> {

    private LazyIconFragment.Flag[] mFlags = LazyIconFragment.Flag.values();

    private Context mContext;

    private SorceryPrefs mPrefs;

    public CustomizeTabsAdapter(Context context) {
        mContext = context;
        mPrefs = SorceryPrefs.getInstance(context);
    }

    @Override
    public TabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TabViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_customize_tabs, parent, false));
    }

    @Override
    public void onBindViewHolder(final TabViewHolder holder, int position) {
        holder.title.setText(ResourceUtil
                .getStringFromResString(mContext, "tab_" + mFlags[position].name().toLowerCase()));
        holder.checkBox
                .setChecked(mPrefs.isTabShow(mFlags[position].name().toLowerCase()).getValue());
        holder.checkBox.setOnCheckedChangeListener(
                (compoundButton, b) -> mPrefs.isTabShow(mFlags[holder.getAdapterPosition()].name().toLowerCase())
                        .setValue(b));
    }

    @Override
    public int getItemCount() {
        return mFlags.length;
    }

    class TabViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_item_customize_tabs_title)
        TextView title;
        @BindView(R.id.checkBox_item_customize_tabs_check)
        CheckBox checkBox;

        TabViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> checkBox.setChecked(!checkBox.isChecked()));
        }
    }
}
