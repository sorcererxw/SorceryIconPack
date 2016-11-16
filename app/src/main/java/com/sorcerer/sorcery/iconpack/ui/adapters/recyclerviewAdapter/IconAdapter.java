package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.activities.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;
import com.sorcerer.sorcery.iconpack.utils.KeyboardUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.mthli.slice.Slice;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/1/19 0019
 */
public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconItemViewHolder> {

    private static final String TAG = "IconAdapter";

    private static final int TYPE_HEADER = 0x0;
    private static final int TYPE_ICON_CENTER = 0x1;
    private static final int TYPE_ICON_TOP = 0x2;
    private static final int TYPE_ICON_TOP_LEFT = 0x3;
    private static final int TYPE_ICON_TOP_RIGHT = 0x4;
    private static final int TYPE_ICON_BOTTOM = 0x5;
    private static final int TYPE_ICON_BOTTOM_LEFT = 0x6;
    private static final int TYPE_ICON_BOTTOM_RIGHT = 0x7;
    private static final int TYPE_ICON_LEFT = 0x8;
    private static final int TYPE_ICON_RIGHT = 0x9;
    private static final int TYPE_ICON_SINGLE_LINE_LEFT = 0xA;
    private static final int TYPE_ICON_SINGLE_LINE_CENTER = 0xB;
    private static final int TYPE_ICON_SINGLE_LINE_RIGHT = 0xC;

    private Activity mActivity;
    private boolean mCustomPicker = false;
    private Context mContext;

    private List<List<IconBean>> mIconBeanLists = new ArrayList<>();
    private List<Pair<IconBean, Integer>> mShowList;

    private boolean mClicked = false;
    private int mSpan;

    public IconAdapter(Activity activity, Context context,
                       List<IconBean> iconBeanList, int span) {

        mActivity = activity;
        mContext = context;
        mSpan = span;

        List<IconBean> tmpList = new ArrayList<>();
        for (int i = 0; i < iconBeanList.size(); i++) {
            if (isLabel(iconBeanList.get(i).getName()) && !tmpList.isEmpty()) {
                mIconBeanLists.add(tmpList);
                tmpList = new ArrayList<>();
            }
            tmpList.add(iconBeanList.get(i));
        }
        mIconBeanLists.add(tmpList);
        generateShowList(span);
    }

    public void setSpan(int span) {
        if (span == mSpan) {
            return;
        }
        mSpan = span;
        generateShowList(span);
    }

    private void generateShowList(int span) {
        mShowList = new ArrayList<>();
        for (int i = 0; i < mIconBeanLists.size(); i++) {
            List<IconBean> list = mIconBeanLists.get(i);
            if (list == null || list.size() == 0) {
                continue;
            }
            boolean withHead = false;
            if (isLabel(list.get(0).getName())) {
                withHead = true;
                mShowList.add(new Pair<>(list.get(0), TYPE_HEADER));
            }
            int itemCount = withHead ? list.size() - 1 : list.size();
            if (itemCount % span != 0) {
                itemCount = (itemCount + span) / span * span;
            }
            for (int position = 0; position < itemCount; position++) {
                int realPos = withHead ? position + 1 : position;
                IconBean bean = null;
                if (realPos < list.size()) {
                    bean = list.get(realPos);
                }
                if (itemCount <= span) {
                    if (position == 0) {
                        mShowList.add(new Pair<>(bean, TYPE_ICON_SINGLE_LINE_LEFT));
                    } else if (position == itemCount - 1) {
                        mShowList.add(new Pair<>(bean, TYPE_ICON_SINGLE_LINE_RIGHT));
                    } else {
                        mShowList.add(new Pair<>(bean, TYPE_ICON_SINGLE_LINE_CENTER));
                    }
                    continue;
                }
                if (position == 0) {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_TOP_LEFT));
                } else if (position == span - 1) {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_TOP_RIGHT));
                } else if (position == itemCount - span) {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_BOTTOM_LEFT));
                } else if (position == itemCount - 1) {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_BOTTOM_RIGHT));
                } else if (position < span) {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_TOP));
                } else if (position + span >= itemCount) {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_BOTTOM));
                } else if ((position + 1) % span == 0) {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_RIGHT));
                } else if (position % span == 0) {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_LEFT));
                } else {
                    mShowList.add(new Pair<>(bean, TYPE_ICON_CENTER));
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mShowList.get(position).second;
    }

    public IconItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_icon_header_new, parent, false);
            return new HeaderViewHolder(view);
        }
        int layout;
        switch (viewType) {
            case TYPE_ICON_SINGLE_LINE_CENTER:
                layout = R.layout.item_icon_single_line_center;
                break;
            case TYPE_ICON_SINGLE_LINE_LEFT:
                layout = R.layout.item_icon_single_line_left;
                break;
            case TYPE_ICON_SINGLE_LINE_RIGHT:
                layout = R.layout.item_icon_single_line_right;
                break;
            case TYPE_ICON_CENTER:
                layout = R.layout.item_icon_center;
                break;
            case TYPE_ICON_TOP:
                layout = R.layout.item_icon_top;
                break;
            case TYPE_ICON_TOP_LEFT:
                layout = R.layout.item_icon_top_left;
                break;
            case TYPE_ICON_TOP_RIGHT:
                layout = R.layout.item_icon_top_right;
                break;
            case TYPE_ICON_BOTTOM:
                layout = R.layout.item_icon_bottom;
                break;
            case TYPE_ICON_BOTTOM_LEFT:
                layout = R.layout.item_icon_bottom_left;
                break;
            case TYPE_ICON_BOTTOM_RIGHT:
                layout = R.layout.item_icon_bottom_right;
                break;
            case TYPE_ICON_LEFT:
                layout = R.layout.item_icon_left;
                break;
            case TYPE_ICON_RIGHT:
                layout = R.layout.item_icon_right;
                break;
            default:
                layout = R.layout.item_icon_center;
        }
        return new IconViewHolder(LayoutInflater.from(mContext).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(IconItemViewHolder holder, int position) {
        final int type = mShowList.get(position).second;
        final IconBean iconBean = mShowList.get(position).first;
        if (type == TYPE_HEADER) {
            final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.mHeader.setText(getLabel(iconBean.getName()));
        } else {
            final IconViewHolder iconHolder = (IconViewHolder) holder;

            if (iconBean != null) {
                iconHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mCustomPicker) {
                            KeyboardUtil.closeKeyboard((Activity) mContext);
                            if (mClicked) {
                                return;
                            }
                            lock(view);
                            showIconDialog(iconHolder,
                                    iconHolder.getAdapterPosition());
                        } else {
                            KeyboardUtil.closeKeyboard((Activity) mContext);
                            ((MainActivity)mActivity).onReturnCustomPickerRes(
                                    mShowList.get(iconHolder.getAdapterPosition()).first.getRes());
                        }
                    }
                });
                Glide.with(mContext)
                        .load(iconBean.getRes())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .animate(android.R.anim.fade_in)
                        .into(iconHolder.mIcon);
            } else {
                iconHolder.itemView.setOnClickListener(null);
                iconHolder.mIcon.setImageBitmap(null);
            }

            boolean leftBottomRect = false,
                    rightBottomRect = false,
                    leftTopRect = false,
                    rightTopRect = false,
                    leftEdgeShadow = false,
                    rightEdgeShadow = false,
                    bottomEdgeShadow = false,
                    topEdgeShadow = false;

            switch (type) {
                case TYPE_ICON_CENTER:
                    break;
                case TYPE_ICON_TOP:
                    topEdgeShadow = true;
                    break;
                case TYPE_ICON_TOP_LEFT:
                    topEdgeShadow = true;
                    leftEdgeShadow = true;
                    leftTopRect = true;
                    break;
                case TYPE_ICON_TOP_RIGHT:
                    topEdgeShadow = true;
                    rightEdgeShadow = true;
                    rightTopRect = true;
                    break;
                case TYPE_ICON_BOTTOM:
                    bottomEdgeShadow = true;
                    break;
                case TYPE_ICON_BOTTOM_LEFT:
                    bottomEdgeShadow = true;
                    leftEdgeShadow = true;
                    leftBottomRect = true;
                    break;
                case TYPE_ICON_BOTTOM_RIGHT:
                    rightEdgeShadow = true;
                    bottomEdgeShadow = true;
                    rightBottomRect = true;
                    break;
                case TYPE_ICON_LEFT:
                    leftEdgeShadow = true;
                    break;
                case TYPE_ICON_RIGHT:
                    rightEdgeShadow = true;
                    break;
                case TYPE_ICON_SINGLE_LINE_LEFT:
                    topEdgeShadow = true;
                    bottomEdgeShadow = true;
                    leftEdgeShadow = true;
                    leftTopRect = true;
                    leftBottomRect = true;
                    break;
                case TYPE_ICON_SINGLE_LINE_CENTER:
                    topEdgeShadow = true;
                    bottomEdgeShadow = true;
                    break;
                case TYPE_ICON_SINGLE_LINE_RIGHT:
                    topEdgeShadow = true;
                    bottomEdgeShadow = true;
                    rightBottomRect = true;
                    rightTopRect = true;
                    rightEdgeShadow = true;
                    break;
            }

            iconHolder.slice.showLeftBottomRect(!leftBottomRect);
            iconHolder.slice.showRightBottomRect(!rightBottomRect);
            iconHolder.slice.showLeftTopRect(!leftTopRect);
            iconHolder.slice.showRightTopRect(!rightTopRect);
            iconHolder.slice.showLeftEdgeShadow(leftEdgeShadow);
            iconHolder.slice.showRightEdgeShadow(rightEdgeShadow);
            iconHolder.slice.showBottomEdgeShadow(bottomEdgeShadow);
            iconHolder.slice.showTopEdgeShadow(topEdgeShadow);
        }
    }

    @Override
    public int getItemCount() {
        return mShowList.size();
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

    public boolean isItemHead(int position) {
        return mShowList.get(position).second == TYPE_HEADER;
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
                mShowList.get(position).first.getRes());
        intent.putExtra(IconDialogActivity.EXTRA_NAME,
                mShowList.get(position).first.getName());
        intent.putExtra(IconDialogActivity.EXTRA_LABEL, mShowList.get(position).first.getLabel());
        if (Build.VERSION.SDK_INT >= 21) {
            ((Activity) mContext).startActivityForResult(
                    intent,
                    MainActivity.REQUEST_ICON_DIALOG,
                    ActivityOptions.makeSceneTransitionAnimation(
                            mActivity,
                            holder.mIcon,
                            "icon"
                    ).toBundle()
            );
        } else {
            ((Activity) mContext).startActivityForResult(intent,
                    MainActivity.REQUEST_ICON_DIALOG);
            ((Activity) mContext).overridePendingTransition(R.anim.fast_fade_in, 0);
        }
    }

    class IconItemViewHolder extends RecyclerView.ViewHolder {

        IconItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class IconViewHolder extends IconItemViewHolder {

        @BindView(R.id.frameLayout_item_icon_container)
        FrameLayout mFrame;

        @BindView(R.id.imageView_item_icon)
        ImageView mIcon;

        Slice slice;

        IconViewHolder(View itemView) {
            super(itemView);
            slice = new Slice(mFrame);
            slice.setRadius(2);
            slice.setRipple(0);
        }
    }

    class HeaderViewHolder extends IconItemViewHolder {
        @BindView(R.id.textView_icon_header_new)
        TextView mHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}


