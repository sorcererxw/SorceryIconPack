package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.IconBean;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/1
 */

public class NewIconAdapter extends RecyclerView.Adapter<NewIconAdapter.IconItemViewHolder> {

    private int mColumnCount;

    private Context mContext;

    private List<IconBean> mIconBeanList;

    public NewIconAdapter(Context context, int columnCount, List<IconBean> iconBeanList) {
        mContext = context;
        mColumnCount = columnCount;
        mIconBeanList = iconBeanList;
    }

    static class IconItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public IconItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mImageView =
                    (ImageView) View.inflate(itemView.getContext(), R.layout.item_icon_new, null)
                            .findViewById(R.id.imageView_icon_content_new);
        }

        private List<ImageView> mImageViews;

//        public List<ImageView> getIcons(int size) {
//            if (mImageViews.size() < size) {
//                for (int i = mImageViews.size(); i <= size; i++) {
//                    mImageViews.add(mImageView);
//                }
//            } else {
//                for (int i = size; i <= mImageViews.size(); i++) {
//                    mImageViews
//                }
//            }
//        }
    }

    @Override
    public IconItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(IconItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mIconBeanList.size() / mColumnCount + 1;
    }


}