package com.sorcerer.sorcery.iconpack.ui.views.bottomSheet;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.sorcerer.sorcery.iconpack.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/9
 */

public class BottomSheetDialogBuilder {
    public static final int MODE_LIST = 0x0;
    public static final int MODE_GRID = 0x1;

    private Context mContext;
    private String mTitle = null;
    private List<MenuItem> mMenuItemList = null;
    private boolean mAutoClose = true;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private boolean mCancelable = true;
    private View mContentView = null;

    private FixedBottomSheetDialog mDialog;

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(MenuItem item);
    }

    public BottomSheetDialogBuilder(Context context) {
        mContext = context;
    }

    public BottomSheetDialogBuilder withTitle(String title) {
        mTitle = title;
        return this;
    }

    public BottomSheetDialogBuilder withAutoClose(boolean autoClose) {
        mAutoClose = autoClose;
        return this;
    }

    public BottomSheetDialogBuilder withCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    public BottomSheetDialogBuilder withOnItemClickListener(
            OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public BottomSheetDialogBuilder withContentView(View view) {
        mContentView = view;
        return this;
    }

    public BottomSheetDialogBuilder withOnItemLongClickListener(
            OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
        return this;
    }

    public BottomSheetDialogBuilder withMenu(int menuRes) {
        if (mMenuItemList == null) {
            mMenuItemList = new ArrayList<>();
        }
        Menu menu = new MenuBuilder(mContext);
        new MenuInflater(mContext).inflate(menuRes, menu);
        mMenuItemList.addAll(Stream.range(0, menu.size())
                .map(menu::getItem)
                .collect(Collectors.toList()));
        return this;
    }

    public BottomSheetDialogBuilder withMenuItem(MenuItem... items) {
        if (mMenuItemList == null) {
            mMenuItemList = new ArrayList<>();
        }
        mMenuItemList.addAll(Arrays.asList(items));
        return this;
    }

    public BottomSheetDialog build() {
        mDialog = new FixedBottomSheetDialog(mContext);
        if (mTitle != null) {
            mDialog.setTitle(mTitle);
        }
        mDialog.setCancelable(mCancelable);

        if (mContentView != null) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(mContentView);

            ScrollView scrollView = new ScrollView(mContext);
            scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mDialog.setContentView(scrollView);
        } else if (mOnItemLongClickListener != null) {
            RecyclerView recyclerView = new RecyclerView(mContext);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new ItemAdapter());
            mDialog.setContentView(recyclerView);
        }
        return mDialog;
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
            return new ItemViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_bottom_sheet, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            MenuItem item = mMenuItemList.get(position);
            if (item == null) {
                return;
            }
            if (item.getIcon() == null) {
                holder.icon.setVisibility(View.INVISIBLE);
            } else {
                holder.icon.setImageDrawable(item.getIcon());
                holder.icon.setVisibility(View.VISIBLE);
            }
            holder.text.setText(item.getTitle());
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> {
                    mOnItemClickListener.onItemClick(item);
                    if (mAutoClose) {
                        mDialog.dismiss();
                    }
                });
            }
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(v -> {
                    boolean result = mOnItemLongClickListener.onItemLongClick(item);
                    if (mAutoClose) {
                        mDialog.dismiss();
                    }
                    return result;
                });
            }
        }

        @Override
        public int getItemCount() {
            return mMenuItemList.size();
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_item_bottom_sheet)
        ImageView icon;

        @BindView(R.id.textView_item_bottom_sheet)
        TextView text;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
