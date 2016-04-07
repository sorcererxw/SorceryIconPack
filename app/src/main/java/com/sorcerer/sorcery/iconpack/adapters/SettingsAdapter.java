package com.sorcerer.sorcery.iconpack.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.CheckSettingsItem;
import com.sorcerer.sorcery.iconpack.models.SettingsItem;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Sorcerer on 2016/2/15 0015.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private List<SettingsItem> mSettingsItems;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {

        private View main;
        private TextView title;
        private TextView summary;
        private SwitchCompat check;

        public ViewHolder(View itemView) {
            super(itemView);
            main = itemView;
            title = (TextView) findViewById(R.id.textView_settings_title);
            summary = (TextView) findViewById(R.id.textView_settings_summary);
            check = (SwitchCompat) findViewById(R.id.switchCompat_settings_check);
        }

        private View findViewById(@IdRes int id) {
            return itemView.findViewById(id);
        }
    }

    public SettingsAdapter(Context context, List<SettingsItem> settingsItems) {
        mContext = context;
        mSettingsItems = settingsItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_settings, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SettingsItem item = mSettingsItems.get(position);

        holder.title.setText(item.getTitle());
        if (item.getSummary() != null) {
            holder.summary.setVisibility(View.VISIBLE);
            holder.summary.setText(item.getSummary());
        }

        if (item instanceof CheckSettingsItem) {
            final CheckSettingsItem tmp = (CheckSettingsItem) item;
            holder.check.setVisibility(View.VISIBLE);
            holder.check.setChecked(tmp.isChecked());
            holder.check.setClickable(false);
            if (tmp.getOnCheckListener() != null) {
                holder.check
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                                         boolean isChecked) {
                                if (isChecked) {
                                    tmp.getOnCheckListener().onChecked();
                                } else {
                                    tmp.getOnCheckListener().onUnchecked();
                                }
                            }
                        });
            }
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.check.setChecked(!holder.check.isChecked());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSettingsItems.size();
    }
}
