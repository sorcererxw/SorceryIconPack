package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Sorcerer on 2016/2/5 0005.
 */
public class AutoLoadRecyclerView extends RecyclerView implements LoadFinishCallBack{

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoadingMore;

    public AutoLoadRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadRecyclerView(Context context,
                                @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnPauseListenerParams(ImageLoader imageLoader, boolean pauseOnScroll, boolean
            pauseOnFling){
        addOnScrollListener(new AutoLoadScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void loadFinish(Object obj) {
        isLoadingMore = false;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    private class AutoLoadScrollListener extends RecyclerView.OnScrollListener {

        private ImageLoader mImageLoader;
        private boolean mPauseOnScroll;
        private boolean mPauseOnFling;

        public AutoLoadScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean
                pauseOnFling) {
            mImageLoader = imageLoader;
            mPauseOnScroll = pauseOnScroll;
            mPauseOnFling = pauseOnFling;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager())
                        .findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();

                if (mOnLoadMoreListener != null && !isLoadingMore &&
                        lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    mOnLoadMoreListener.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
            if (mImageLoader != null) {
                switch (newState) {
                    case 0:
                        mImageLoader.resume();
                        break;
                    case 1:
                        if (mPauseOnScroll) {
                            mImageLoader.pause();
                        } else {
                            mImageLoader.resume();
                        }
                        break;
                    case 2:
                        if (mPauseOnFling) {
                            mImageLoader.pause();
                        } else {
                            mImageLoader.resume();
                        }
                        break;
                }
            }
        }
    }
}
