package com.sorcerer.sorcery.iconpack.settings.licenses.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.settings.licenses.models.OpenSourceLibBean;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/10
 */

class OpenSourceLibAdapter
        extends RecyclerView.Adapter<OpenSourceLibAdapter.OpenSourceLibViewHolder> {
    private Context mContext;

    private List<OpenSourceLibBean> mDataList;

    OpenSourceLibAdapter(Context context, List<OpenSourceLibBean> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public OpenSourceLibViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OpenSourceLibViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_opensourcelib, parent, false));
    }

    @Override
    public void onBindViewHolder(final OpenSourceLibViewHolder holder, int position) {
        if (mDataList.get(position).getLicense().length() > 600) {
            holder.license.setText(mDataList.get(position).getLicense().substring(0, 600));
            holder.arrow.setVisibility(View.VISIBLE);
            holder.arrow.animate().rotation(0).setDuration(0).start();
        } else {
            holder.arrow.setVisibility(View.GONE);
            holder.license.setText(mDataList.get(position).getLicense());
        }
        holder.name.setText(mDataList.get(position).getName());
        holder.author.setText(mDataList.get(position).getAuthor());

        holder.arrow.setOnClickListener(view -> {
            if (holder.license.length() > 600) {
                holder.license.setText(mDataList.get(holder.getAdapterPosition()).getLicense()
                        .substring(0, 600));
            } else {
                holder.license.setText(mDataList.get(holder.getAdapterPosition()).getLicense());
            }
            holder.arrow.animate().rotationBy(180).setDuration(250).start();
        });

        holder.itemView.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mDataList.get(holder.getAdapterPosition()).getLink()));
            mContext.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class OpenSourceLibViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_item_openSourceLib_author)
        TextView author;

        @BindView(R.id.textView_item_openSourceLib_license)
        TextView license;

        @BindView(R.id.textView_item_openSourceLib_name)
        TextView name;

        @BindView(R.id.imageView_item_openSourceLib_arrow)
        ImageView arrow;

        OpenSourceLibViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            arrow.setImageDrawable(new IconicsDrawable(itemView.getContext(),
                    GoogleMaterial.Icon.gmd_keyboard_arrow_down)
                    .sizeDp(12)
                    .color(ResourceUtil.getAttrColor(itemView.getContext(),
                            android.R.attr.textColorSecondary)));
        }
    }
}
