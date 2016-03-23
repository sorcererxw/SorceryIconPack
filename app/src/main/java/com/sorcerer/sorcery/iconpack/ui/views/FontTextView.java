package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.renderscript.Type;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sorcerer on 2016/3/1 0001.
 */
public class FontTextView extends TextView {
    public FontTextView(Context context) {
        super(context);
        init(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font.TTF");
        setTypeface(typeface);
    }
}
