package com.sorcerer.sorcery.iconpack.ui.activities;

import android.graphics.Point;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MenuItem;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.HelpAdapter;

import butterknife.BindView;

public class HelpActivity extends SlideInAndOutAppCompatActivity {

    @BindView(R.id.recyclerView_help)
    RecyclerView mRecyclerView;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_help;
    }

    private HelpAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void init() {
        super.init();

        setToolbarBackIndicator();

        mGridLayoutManager =
                new GridLayoutManager(this, calcNumOfRows(), LinearLayoutManager.VERTICAL, false);

        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mAdapter = new HelpAdapter(this, calcNumOfRows());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

    private void resize() {
        mGridLayoutManager.setSpanCount(calcNumOfRows());
        mGridLayoutManager.requestLayout();
        mAdapter.changeSpan(calcNumOfRows());
    }

    private int calcNumOfRows() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float s = getResources().getDimension(R.dimen.help_item_size);
//                + 2 * getResources().getDimension(R.dimen.icon_grid_item_margin);
        return Math.max(1, (int) (size.x / s));
    }

}
