package com.sorcerer.sorcery.iconpack.ui.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.RequestAdapter;
import com.sorcerer.sorcery.iconpack.util.Utility;

/**
 * Created by Sorcerer on 2016/2/6 0006.
 */
public class RequestDialogView extends ViewGroup {

    public RequestDialogView(Context context) {
        super(context);
        init();
    }

    public RequestDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RequestDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init() {
        this.removeAllViews();
        View recycler = LayoutInflater.from(getContext()).inflate(R.layout.layout_icon_request,
                this, false);
        RecyclerView recyclerView =
                (RecyclerView) recycler.findViewById(R.id.recyclerView_icon_request);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        recyclerView.setAdapter(
                new RequestAdapter(getContext(), Utility.getComponentInfo(getContext(), true))
        );
        this.addView(recycler);
    }

}
