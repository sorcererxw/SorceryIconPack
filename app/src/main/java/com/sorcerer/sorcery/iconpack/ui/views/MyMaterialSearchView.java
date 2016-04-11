package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.Filter;
import android.widget.FrameLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * Created by Sorcerer on 2016/4/11 0011.
 */
public class MyMaterialSearchView extends MaterialSearchView {

    private OnMenuItemClickCallback mOnMenuItemClickCallback;

    public interface OnMenuItemClickCallback {
        void callBack();
    }

    public MyMaterialSearchView(Context context) {
        super(context);
        init();
    }

    public MyMaterialSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyMaterialSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void init() {

    }

    /**
     * Call this method and pass the menu item so this class can handle click events for the Menu Item.
     *
     * @param menuItem
     */
    public void setMenuItem(MenuItem menuItem) {
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mOnMenuItemClickCallback != null) {
                    mOnMenuItemClickCallback.callBack();
                }
                showSearch();
                return true;
            }
        });
    }

    public void setOnMenuItemClickCallback(OnMenuItemClickCallback callback) {
        mOnMenuItemClickCallback = callback;
    }

}
