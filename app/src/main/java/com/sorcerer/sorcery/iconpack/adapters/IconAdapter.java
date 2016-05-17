package com.sorcerer.sorcery.iconpack.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SIP;
import com.sorcerer.sorcery.iconpack.models.AppInfo;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;
import com.sorcerer.sorcery.iconpack.ui.fragments.IconFragment;
import com.sorcerer.sorcery.iconpack.ui.views.LikeLayout;
import com.sorcerer.sorcery.iconpack.ui.activities.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.util.AppInfoUtil;
import com.sorcerer.sorcery.iconpack.util.DisplayUtil;
import com.sorcerer.sorcery.iconpack.util.ImageUtil;
import com.sorcerer.sorcery.iconpack.util.Utility;

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
    private List<IconBean> mIconBeanList = new ArrayList<>();
    private List<IconBean> mShowIconList = new ArrayList<>();
    private boolean mClicked = false;
    private RecyclerView mParent;

    public final static class IconItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public View main;
        public TextView header;
        public boolean isViewStubInflate = false;

        public IconItemViewHolder(View itemView) {
            super(itemView);

            main = itemView;
            icon = (ImageView) main.findViewById(R.id.imageView_item_icon_grid);
        }

        public void inflateHeader() {
            View view = ((ViewStub) (main.findViewById(R.id.viewStub_item_icon_header))).inflate();
            header = (TextView) view.findViewById(R.id.textView_item_icon_header);
            isViewStubInflate = true;
        }

        public void showHeader(String s) {
            main.setClickable(false);

            int cnt = 0;

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '#') {
                    cnt++;
                } else {
                    break;
                }
            }

            if (!isViewStubInflate) {
                inflateHeader();
            }
            if (isViewStubInflate && header != null) {
                header.setVisibility(View.VISIBLE);
//                header.setTextSize(DisplayUtil.dip2px(header.getContext(), 12- 2 * cnt));
                if (cnt == 1) {
                    header.setTextSize(header.getContext().getResources().getDimension(R.dimen
                            .icon_header_first_level));
                } else {
                    header.setTextSize(header.getContext().getResources().getDimension(R.dimen
                            .icon_header_second_level));
                }
                header.setText(s.substring(cnt, s.length()));
            }
        }

        public void hideHeader() {
            if (isViewStubInflate && header != null) {
                header.setVisibility(View.GONE);
            }
        }

        public void showIcon() {
            main.setClickable(true);
            icon.setVisibility(View.VISIBLE);
        }

        public void hideIcon() {
            icon.setVisibility(View.GONE);
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

    public IconItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_icon_grid, mParent, false);
        return new IconItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IconItemViewHolder holder, final int position) {
        if (mShowIconList.get(position).getName().contains("baidu")) {
            ImageUtil.grayScale(holder.icon);
        } else {
            ImageUtil.resetScale(holder.icon);
        }
        if (isLabel(mShowIconList.get(position).getName())) {
            holder.hideIcon();
            holder.showHeader(getLabel(mShowIconList.get(position).getName()));
        } else {
            holder.showIcon();
            holder.hideHeader();

            if (!mCustomPicker) {
                holder.main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();

                        if (mClicked) {
                            return;
                        }
                        lock(v);
                        showIconDialog(holder, position);
                    }
                });
            } else {
                holder.main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();
                        returnIconResource(position);
                    }
                });
            }
            ImageLoader.getInstance()
                    .displayImage("drawable://" + mShowIconList.get(position).getRes(),
                            holder.icon,
                            SIP.mOptions);
//            setAnimation(holder.icon);

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

    //动画加载
    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                R.anim.scale_in);
        viewToAnimate.startAnimation(animation);
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

    private void showIconDialog(final IconItemViewHolder holder, final int position) {
        if (Build.VERSION.SDK_INT < 21 && false) {
            View iconDialog = View.inflate(mContext, R.layout.dialog_icon_show, null);
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

        } else {
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
    }

    private void returnIconResource(final int position) {
        Intent intent = new Intent();
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    mShowIconList.get(position).getRes());
        } catch (Exception e) {
            e.printStackTrace();
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

    private void closeKeyboard(){
        closeKeyboard((Activity)mParent.getContext());
    }

    private void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
