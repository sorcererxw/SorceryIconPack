package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;
import com.sorcerer.sorcery.iconpack.ui.activities.IconDialogActivity;
import com.sorcerer.sorcery.iconpack.ui.activities.MainActivity;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/7
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {


    private List<IconBean> mDataList;
    private List<IconBean> mShowList;

    private Activity mActivity;

    public SearchAdapter(Activity activity) {
        mActivity = activity;
        mDataList = new ArrayList<>();
        mShowList = new ArrayList<>();
    }

    public void setData(List<IconBean> dataList) {
        mDataList = dataList;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(
                LayoutInflater.from(mActivity).inflate(R.layout.item_icon_center, parent, false));
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        if (position < mShowList.size()) {
            Glide.with(mActivity)
                    .load(mShowList.get(position).getRes())
                    .into(holder.icon);

            if (mCustomPicker) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mActivity.setResult(Activity.RESULT_OK,
                                new Intent().putExtra("icon res",
                                        mShowList.get(holder.getAdapterPosition()).getRes()));
                        mActivity.finish();
                    }
                });
            } else {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showIconDialog(holder.icon, mShowList.get(holder.getAdapterPosition()));
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mShowList.size();
    }

    public static final int SEARCH_CODE_OK = 0x0;
    public static final int SEARCH_CODE_INVALID_INPUT = 0x1;
    public static final int SEARCH_CODE_EMPTY = 0x2;
    public static final int SEARCH_CODE_NOT_FOUND = 0x3;

    public interface SearchCallback {
        void call(int code);
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.frameLayout_item_icon_container)
        FrameLayout container;

        @BindView(R.id.imageView_item_icon)
        ImageView icon;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @SuppressLint("SetTextI18n")
    public void search(String s, SearchCallback searchCallback) {
        mShowList.clear();

        if (s == null || s.length() == 0) {
            searchCallback.call(SEARCH_CODE_EMPTY);
        } else if (!s.matches("[0-9a-zA-Z]+")) {
            searchCallback.call(SEARCH_CODE_INVALID_INPUT);
        } else {
            for (IconBean iconBean : mDataList) {
                if (iconBean.getLabel().contains(s)) {
                    mShowList.add(iconBean);
                }
            }
            if (mShowList.size() <= 0) {
                searchCallback.call(SEARCH_CODE_NOT_FOUND);
            } else {
                searchCallback.call(SEARCH_CODE_OK);
            }
        }
    }

    private void showIconDialog(ImageView icon, IconBean iconBean) {
        Intent intent = new Intent(mActivity, IconDialogActivity.class);
        intent.putExtra(IconDialogActivity.EXTRA_RES, iconBean.getRes());
        intent.putExtra(IconDialogActivity.EXTRA_NAME, iconBean.getName());
        intent.putExtra(IconDialogActivity.EXTRA_LABEL, iconBean.getLabel());
        if (Build.VERSION.SDK_INT >= 21) {
            mActivity.startActivityForResult(
                    intent,
                    MainActivity.REQUEST_ICON_DIALOG,
                    ActivityOptions.makeSceneTransitionAnimation(
                            mActivity,
                            icon,
                            "icon"
                    ).toBundle()
            );
        } else {
            mActivity.startActivityForResult(intent, MainActivity.REQUEST_ICON_DIALOG);
            mActivity.overridePendingTransition(R.anim.fast_fade_in, 0);
        }
    }

    private boolean mCustomPicker = false;

    public void setCustomPicker(boolean customPicker) {
        mCustomPicker = customPicker;
    }

}
