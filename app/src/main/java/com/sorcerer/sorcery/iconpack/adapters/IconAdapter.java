package com.sorcerer.sorcery.iconpack.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SIP;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.models.IconBmob;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;
import com.sorcerer.sorcery.iconpack.ui.activities.IconDialogActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sorcerer on 2016/1/19 0019.
 */
public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconItemViewHolder> {

    private static final String TAG = "IconAdapter";

    private Activity mActivity;
    private boolean mCustomPicker = false;
    private Context mContext;
    private int lastPosition = -1;
    private List<IconBean> mIconBeanList = new ArrayList<>();
    private List<IconBean> mShowIconList = new ArrayList<>();
    private View mLastClickItem = null;

    public final static class IconItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public View main;

        public IconItemViewHolder(View itemView) {
            super(itemView);

            main = itemView;
            icon = (ImageView) main.findViewById(R.id.imageView_item_icon_grid);
        }
    }

    public IconAdapter(Activity activity, Context context, int flag) {
        mActivity = activity;
        mContext = context;
        loadIcons(flag);
    }

    private void loadIcons(int flag) {
        final String packageName = mContext.getPackageName();
        addIcons(mContext.getResources(), packageName, flag);

//        Log.d(TAG, "" + mIconNames.length);
//        Log.d(TAG, "" + mItems.size());

        mShowIconList.clear();
        mShowIconList.addAll(mIconBeanList);
    }

    private void addIcons(Resources resources, String packageName, int flag) {
        String[] mIconNames;

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
            case IconFragment.FLAG_FLYME:
                mIconNames = resources.getStringArray(R.array.icon_pack_flyme);
                break;
            default:
                mIconNames = new String[]{""};
        }
        for (String name : mIconNames) {
            IconBean iconBean = new IconBean(name);

            int res = resources.getIdentifier(name, "drawable", packageName);

            if (res != 0) {
                final int thumbRes = resources.getIdentifier(name, "drawable", packageName);
                if (thumbRes != 0) {
                    iconBean.setRes(thumbRes);
                } else {
                    Log.d(TAG, "thumb = 0: " + name);
                }
            } else {
                Log.d(TAG, "res = 0: " + name);
            }

            mIconBeanList.add(iconBean);
        }
    }

    public IconItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_icon_grid, parent, false);
        return new IconItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IconItemViewHolder holder, final int position) {
        if (!mCustomPicker) {
            if (false && Build.VERSION.SDK_INT < 21) {
                holder.main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View iconDialog = View.inflate(mContext, R.layout.dialog_icon_show, null);
//                ((TextView) iconDialog.findViewById(R.id.textView_dialog_title))
//                        .setText(mShowIconList.get(position).getLabel());
                        ((ImageView) iconDialog.findViewById(R.id.imageView_dialog_icon))
                                .setImageResource(mShowIconList.get(position).getRes());
                        ((LikeLayout) iconDialog.findViewById(R.id.likeLayout))
                                .bindIcon(mShowIconList.get
                                        (position).getName());
                        if (mIconBeanList.get(position).getLabel().equals("google plus")) {
                            Button join =
                                    (Button) iconDialog.findViewById(R.id.button_dialog_icon_join);
                            join.setVisibility(View.VISIBLE);
                            join.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                                            ("https://plus.google.com/communities/115317471515103046699"));
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                        new MaterialDialog.Builder(mContext)
                                .customView(iconDialog, false)
                                .title(mShowIconList.get(position).getLabel())
                                .show();
                    }
                });
            } else {
                holder.main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lock(v);

                        Intent intent = new Intent(mContext, IconDialogActivity.class);

                        intent.putExtra(IconDialogActivity.EXTRA_RES,
                                mShowIconList.get(position).getRes());
                        intent.putExtra(IconDialogActivity.EXTRA_NAME,
                                mShowIconList.get(position).getName());
                        intent.putExtra(IconDialogActivity.EXTRA_LABEL, mShowIconList.get
                                (position).getLabel());

                        if (Build.VERSION.SDK_INT >= 21) {
                            mActivity.startActivityForResult(intent,
                                    MainActivity.REQUEST_ICON_DIALOG,
                                    ActivityOptions.makeSceneTransitionAnimation(mActivity,
                                            holder.icon,
                                            "icon").toBundle());
                        } else {
                            mActivity.startActivityForResult(intent,
                                    MainActivity.REQUEST_ICON_DIALOG);
                            mActivity.overridePendingTransition(android.R.anim.fade_in, 0);
                        }
                    }
                });
            }
        } else {
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bitmap bitmap = null;

                    try {
                        bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                mShowIconList.get(position).getRes());
                    } catch (Exception e) {
                    }

                    if (bitmap != null) {
                        intent.putExtra("icon", bitmap);
                        intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
                                mShowIconList.get(position).getRes());
                        String bmUri = "android.resource://" + mContext.getPackageName() + "/" +
                                String.valueOf(mShowIconList.get(position).getRes());
                        intent.setData(Uri.parse(bmUri));
                        mActivity.setResult(Activity.RESULT_OK, intent);
                    } else {
                        mActivity.setResult(Activity.RESULT_CANCELED, intent);
                    }
                    mActivity.finish();
                }
            });
        }
        holder.icon.setImageResource(mShowIconList.get(position).getRes());
        setAnimation(holder.icon, 0);
//        ImageLoader.getInstance().displayImage("drawable://" + mItems.get(position), holder.icon,
//                SIP.mOptions);
//        setAnimation(holder.icon, position);
    }

    //动画加载
    private void setAnimation(View viewToAnimate, int position) {
//        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
//                    android.R.anim.fade_in);
        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                R.anim.scale_in);
        viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
    }

    @Override
    public int getItemCount() {
        return mShowIconList.size();
    }

    public void showWithString(String s) {
        if (s.isEmpty() && mShowIconList.size() == mIconBeanList.size()) {
            return;
        }

        mShowIconList.clear();
        if (s.isEmpty()) {
            for (int i = 0; i < mIconBeanList.size(); i++) {
                mShowIconList.add(mIconBeanList.get(i));
            }
        } else {
            for (int i = 0; i < mIconBeanList.size(); i++) {
                if (mIconBeanList.get(i).getLabel().contains(s)) {
                    mShowIconList.add(mIconBeanList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }


    public boolean isCustomPicker() {
        return mCustomPicker;
    }

    public void setCustomPicker(Activity activity, boolean customPicker) {
        mActivity = activity;
        mCustomPicker = customPicker;
    }

    public void lock(View view) {
        if (mLastClickItem != null && !mLastClickItem.isClickable()) {
            mLastClickItem.setClickable(true);
        }
        mLastClickItem = view;
        mLastClickItem.setClickable(false);
    }

    public void unlock() {
        if (mLastClickItem != null) {
            mLastClickItem.setClickable(true);
        }
    }

}
