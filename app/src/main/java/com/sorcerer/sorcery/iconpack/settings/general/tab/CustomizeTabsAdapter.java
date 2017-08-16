package com.sorcerer.sorcery.iconpack.settings.general.tab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.iconShowCase.overview.IconFlag;
import com.sorcerer.sorcery.iconpack.ui.adapters.BaseRecyclerViewAdapter;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/20
 */

class CustomizeTabsAdapter extends BaseRecyclerViewAdapter<CustomizeTabsAdapter.TabViewHolder> {

    private IconFlag[] mFlags = IconFlag.values();

    private Context mContext;

    public CustomizeTabsAdapter(Context context) {
        super();
        mContext = context;
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
        Boolean show = mPrefs.isTabShow(mFlags[position].name().toLowerCase()).get();
        holder.checkBox.setChecked(show == null ? true : show);
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) ->
                mPrefs.isTabShow(mFlags[position].name().toLowerCase()).set(b));
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
