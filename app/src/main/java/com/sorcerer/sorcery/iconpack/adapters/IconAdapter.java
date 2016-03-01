package com.sorcerer.sorcery.iconpack.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.models.IconBmob;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sorcerer on 2016/1/19 0019.
 */
public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconItemViewHolder> {

    private static final String TAG = "IconAdapter";

    private String[] mIconNames;
    private Context mContext;
    private List<Integer> mItems;
    private int lastPosition = -1;
    private List<IconBean> mIconBeanList = new ArrayList<>();
    private List<IconBean> mIconList = new ArrayList();

    public final static class IconItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public View main;

        public IconItemViewHolder(View itemView) {
            super(itemView);

            main = itemView;
            icon = (ImageView) main.findViewById(R.id.imageView_item_icon_grid);
        }
    }

    public IconAdapter(Context context, int flag) {
        mContext = context;
        loadIcons(flag);
    }

    private void loadIcons(int flag) {
        mItems = new ArrayList<>();
        final String packageName = mContext.getPackageName();
        addIcons(mContext.getResources(), packageName, flag);

        Log.d(TAG, "" + mIconNames.length);
        Log.d(TAG, "" + mItems.size());

        for (int i = 0; i < mIconNames.length; i++) {
            Log.d(TAG, i + " " + mIconNames[i] + " " + mItems.get(i));
            mIconBeanList.add(new IconBean(mIconNames[i], mItems.get(i)));
        }
        mIconList.clear();
        mIconList.addAll(mIconBeanList);
    }

    private void addIcons(Resources resources, String packageName, int flag) {
        switch (flag) {
            case IconFragment.FLAG_ALL:
                mIconNames = resources.getStringArray(R.array.icon_pack);
                break;
            case IconFragment.FLAG_NEW:
                mIconNames = resources.getStringArray(R.array.icon_pack_new);
                break;
            case IconFragment.FLAG_ALI:
                mIconNames = resources.getStringArray(R.array.icon_pack_ali);
                break;
            case IconFragment.FLAG_BAIDU:
                mIconNames = resources.getStringArray(R.array.icon_pack_baidu);
                break;
            case IconFragment.FLAG_CYANOGENMOD:
                mIconNames = resources.getStringArray(R.array.icon_pack_cyanogenmod);
                break;
            case IconFragment.FLAG_GOOGLE:
                mIconNames = resources.getStringArray(R.array.icon_pack_google);
                break;
            case IconFragment.FLAG_HTC:
                mIconNames = resources.getStringArray(R.array.icon_pack_htc);
                break;
            case IconFragment.FLAG_LENOVO:
                mIconNames = resources.getStringArray(R.array.icon_pack_lenovo);
                break;
            case IconFragment.FLAG_LG:
                mIconNames = resources.getStringArray(R.array.icon_pack_lg);
                break;
            case IconFragment.FLAG_MICROSOFT:
                mIconNames = resources.getStringArray(R.array.icon_pack_microsoft);
                break;
            case IconFragment.FLAG_MOTO:
                mIconNames = resources.getStringArray(R.array.icon_pack_moto);
                break;
            case IconFragment.FLAG_SAMSUNG:
                mIconNames = resources.getStringArray(R.array.icon_pack_samsung);
                break;
            case IconFragment.FLAG_SONY:
                mIconNames = resources.getStringArray(R.array.icon_pack_sony);
                break;
            case IconFragment.FLAG_TENCENT:
                mIconNames = resources.getStringArray(R.array.icon_pack_tencent);
                break;
            case IconFragment.FLAG_XIAOMI:
                mIconNames = resources.getStringArray(R.array.icon_pack_xiaomi);
                break;
            default:
                mIconNames = new String[]{""};
        }
        for (String name : mIconNames) {
            int res = resources.getIdentifier(name, "drawable", packageName);
            if (res != 0) {
                final int thumbRes = resources.getIdentifier(name, "drawable", packageName);
                if (thumbRes != 0) {
                    mItems.add(thumbRes);
                } else {
                    Log.d(TAG, "thumb = 0: " + name);
                }
            } else {
                Log.d(TAG, "res = 0: " + name);
            }
        }
    }

    public IconItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_icon_grid, parent, false);
        return new IconItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IconItemViewHolder holder, final int position) {
        holder.icon.setImageResource(mIconList.get(position).getRes());
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View iconDialog = View.inflate(mContext, R.layout.dialog_icon_show, null);
//                ((TextView) iconDialog.findViewById(R.id.textView_dialog_title))
//                        .setText(mIconList.get(position).getLabel());
                ((ImageView) iconDialog.findViewById(R.id.imageView_dialog_icon))
                        .setImageResource(mIconList.get(position).getRes());
                ((LikeLayout) iconDialog.findViewById(R.id.likeLayout)).bindIcon(mIconList.get
                        (position).getName());
                new MaterialDialog.Builder(mContext)
                        .customView(iconDialog, false)
                        .title(mIconList.get(position).getLabel())
                        .show();
            }
        });

//        ImageLoader.getInstance().displayImage("drawable://" + mItems.get(position), holder.icon,
//                SIP.mOptions);
//        setAnimation(holder.icon, position);
    }

    //动画加载
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                    android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mIconList.size();
    }

    public void showWithString(String s) {
        mIconList.clear();
        if (s.isEmpty()) {
            for (int i = 0; i < mIconNames.length; i++) {
                mIconList.add(mIconBeanList.get(i));
            }
        } else {
            for (int i = 0; i < mIconNames.length; i++) {
                if (mIconBeanList.get(i).getLabel().contains(s)) {
                    mIconList.add(mIconBeanList.get(i));
                }
            }
        }
        notifyDataSetChanged();

    }

}
