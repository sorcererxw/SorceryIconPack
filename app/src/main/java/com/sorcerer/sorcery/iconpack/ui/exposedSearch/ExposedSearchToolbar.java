package com.sorcerer.sorcery.iconpack.ui.exposedSearch;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

public class ExposedSearchToolbar extends Toolbar {

    public ExposedSearchToolbar(Context context) {
        super(context);
        init(context);
    }

    public ExposedSearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExposedSearchToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.card_noshadow);
        setNavigationIcon(R.drawable.ic_search_grey_600_24dp);
    }
}
