package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sorcerer.sorcery.iconpack.R;

import java.lang.reflect.Field;

/**
 * Created by Sorcerer on 2016/3/2 0002.
 */
public class SwipeSearchLayout extends SwipeRefreshLayout {
    /**
     * Simple constructor to use when creating a SwipeRefreshLayout from code.
     *
     * @param context
     */
    public SwipeSearchLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructor that is called when inflating SwipeRefreshLayout from XML.
     *
     * @param context
     * @param attrs
     */
    public SwipeSearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setSize(LARGE);
        try {
            Field f = super.getClass().getDeclaredField("mCircleView");
            f.setAccessible(true);
            ImageView img = (ImageView) f.get(this);
            img.setImageResource(R.drawable.ic_search_white_24dp);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
