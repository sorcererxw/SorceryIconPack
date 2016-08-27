package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.activities.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;
import com.sorcerer.sorcery.iconpack.util.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/1/19 0019.
 */
public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconItemViewHolder> {

    private static final String TAG = "IconAdapter";

    private static final int ITEM_TYPE_ICON = 0x1;
    private static final int ITEM_TYPE_HEADER = 0x10;

    private Activity mActivity;
    private boolean mCustomPicker = false;
    private Context mContext;
    private List<IconBean> mIconBeanList = new ArrayList<>();
    private List<IconBean> mShowIconList = new ArrayList<>();
    private boolean mClicked = false;
    private RecyclerView mParent;

    class IconItemViewHolder extends RecyclerView.ViewHolder {

        public IconItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class IconViewHolder extends IconItemViewHolder {
        @BindView(R.id.imageView_icon_content_new)
        ImageView mIcon;

        boolean mGrayed = false;

        public IconViewHolder(View itemView) {
            super(itemView);
        }
    }

    private Map<Integer, Boolean> mHeadVisibleMap = new HashMap<>();

    class HeaderViewHolder extends IconItemViewHolder {
        @BindView(R.id.textView_icon_header_new)
        TextView mHeader;

        @BindView(R.id.textView_icon_header_count)
        TextView mCount;


        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public IconAdapter(Activity activity, Context context,
                       List<IconBean> iconBeanList, RecyclerView parent) {
        mParent = parent;

        mActivity = activity;
        mContext = context;
        mIconBeanList = iconBeanList;

        mShowIconList.clear();
        mShowIconList.addAll(mIconBeanList);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLabel(mShowIconList.get(position).getName())) {
            return ITEM_TYPE_HEADER;
        }
        return ITEM_TYPE_ICON;
    }

    public IconItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_icon_header_new, mParent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == ITEM_TYPE_ICON) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_icon_content_new, mParent, false);
            return new IconViewHolder(view);
        }
        return null;
    }

    private String getGroupLength(int index) {
        int cnt = 0;
        for (int i = index + 1; i < mShowIconList.size(); i++) {
            if (isLabel(mShowIconList.get(i).getLabel())) {
                break;
            } else {
                cnt++;
            }
        }
        StringBuilder builder = new StringBuilder(cnt + "");
        while (builder.length() < 5) {
            builder.append(" ");
            builder.insert(0, " ");
        }
        return builder.toString();
    }

    @Override
    public void onBindViewHolder(final IconItemViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_HEADER) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.mHeader.setText(getLabel(mShowIconList.get(position).getName()));
            headerHolder.mCount.setText(getGroupLength(position));
            headerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mHeadVisibleMap.get(headerHolder.getAdapterPosition()) != null
                            && mHeadVisibleMap.get(headerHolder.getAdapterPosition())) {
                        mHeadVisibleMap.put(headerHolder.getAdapterPosition(), false);
                        headerHolder.mCount.setVisibility(View.GONE);
                    } else {
                        mHeadVisibleMap.put(headerHolder.getAdapterPosition(), true);
                        headerHolder.mCount.setVisibility(View.VISIBLE);
                    }
                }
            });
            if (mHeadVisibleMap.get(position) != null && mHeadVisibleMap.get(position)) {
                headerHolder.mCount.setVisibility(View.VISIBLE);
            } else {
                headerHolder.mCount.setVisibility(View.GONE);
            }
        } else if (getItemViewType(position) == ITEM_TYPE_ICON) {
            final IconViewHolder iconHolder = (IconViewHolder) holder;
//            if (mShowIconList.get(position).getName().contains("baidu")) {
//                ImageUtil.grayScale(iconHolder.mIcon);
//                iconHolder.mGrayed = true;
//            } else {
//                if (iconHolder.mGrayed) {
//                    ImageUtil.resetScale(iconHolder.mIcon);
//                    iconHolder.mGrayed = false;
//                }
//            }
            if (!mCustomPicker) {
                iconHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();

                        if (mClicked) {
                            return;
                        }
                        lock(v);
                        showIconDialog(iconHolder, holder.getAdapterPosition());
                    }
                });
            } else {
                iconHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();
                        returnIconResource(holder.getAdapterPosition());
                    }
                });
            }

//            Glide.with(mContext)
//                    .load(mShowIconList.get(position).getRes())
//                    .into(iconHolder.mIcon);

            Glide.with(mContext)
                    .load(mShowIconList.get(position).getRes())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(iconHolder.mIcon) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            iconHolder.mIcon.setImageBitmap(resource);
//                            TransitionDrawable td = new TransitionDrawable(
//                                    new Drawable[]{
//                                            new ColorDrawable(Color.TRANSPARENT),
//                                            new BitmapDrawable(mContext.getResources(),
//                                                    resource)
//                                    });
//                            iconHolder.mIcon.setImageDrawable(td);
//                            td.startTransition(250);
                        }
                    });
        }
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
                if (mIconBeanList.get(i).getLabel().contains(s)
                        && !isLabel(mIconBeanList.get(i).getName())) {
                    mShowIconList.add(mIconBeanList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setCustomPicker(Activity activity, boolean customPicker) {
        mActivity = activity;
        mCustomPicker = customPicker;
    }

    private void lock(View v) {
        mClicked = true;

        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClicked = false;
            }
        }, 500);
    }

    private boolean isLabel(String s) {
        return s.startsWith("**") && s.endsWith("**");
    }

    private String getLabel(String s) {
        return s.substring(2, s.length() - 2);
    }

    private void showIconDialog(IconViewHolder holder, final int position) {
        Intent intent = new Intent(mContext, IconDialogActivity.class);
        intent.putExtra(IconDialogActivity.EXTRA_RES,
                mShowIconList.get(position).getRes());
        intent.putExtra(IconDialogActivity.EXTRA_NAME,
                mShowIconList.get(position).getName());
        intent.putExtra(IconDialogActivity.EXTRA_LABEL, mShowIconList.get(position).getLabel());
        if (Build.VERSION.SDK_INT >= 21) {
            mActivity.startActivityForResult(
                    intent,
                    MainActivity.REQUEST_ICON_DIALOG,
                    ActivityOptions.makeSceneTransitionAnimation(
                            mActivity,
                            holder.mIcon,
                            "icon"
                    ).toBundle()
            );
        } else {
            mActivity.startActivityForResult(intent,
                    MainActivity.REQUEST_ICON_DIALOG);
            mActivity.overridePendingTransition(R.anim.fast_fade_in, 0);
        }
    }

    private void returnIconResource(final int position) {
        Intent intent = new Intent();
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    mShowIconList.get(position).getRes());
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        if (bitmap != null) {
            intent.putExtra("icon", bitmap);
            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE",
                    mShowIconList.get(position).getRes());
            String bmUri = "android.resource://" + mContext.getPackageName() + "/"
                    + String.valueOf(mShowIconList.get(position).getRes());
            intent.setData(Uri.parse(bmUri));
            mActivity.setResult(Activity.RESULT_OK, intent);
        } else {
            mActivity.setResult(Activity.RESULT_CANCELED, intent);
        }
        mActivity.finish();
    }

    private void closeKeyboard() {
        closeKeyboard((Activity) mParent.getContext());
    }

    private void closeKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
