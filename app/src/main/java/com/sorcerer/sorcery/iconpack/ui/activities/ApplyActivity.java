package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.ApplyAdapter;
import com.sorcerer.sorcery.iconpack.util.LauncherApplier;
import com.sorcerer.sorcery.iconpack.util.Utility;

public class ApplyActivity extends SlideInAndOutAppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mApplyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_universal);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mApplyRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_apply);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mApplyRecyclerView.setLayoutManager(layoutManager);
        mApplyRecyclerView.setHasFixedSize(true);
        mApplyRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position % 3 == 0) {
//                    outRect.left = 16;
//                    outRect.right = 16;
                } else if (position % 3 == 1) {
//                    outRect.left = 16;
//                    outRect.right = 16;
                } else if (position % 3 == 2) {
//                    outRect.left = 16;
//                    outRect.right = 16;
                }
            }
        });

        final ApplyAdapter applyAdapter = new ApplyAdapter(this, Utility.generateLauncherInfo
                (this));
        applyAdapter.setOnItemClickListener(new ApplyAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (applyAdapter.getItem(position).isInstalled()) {
                    LauncherApplier
                            .applyLauncher(view.getContext(),
                                    applyAdapter.getItem(position).getLabel()
                                            .split(" ")[0]);
                } else {
                    final String appPackageName =
                            applyAdapter.getItem(position).getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse("https://play.google.com/store/apps/details?id=" +
                                        appPackageName)));
                    }
                }
            }
        });
        mApplyRecyclerView.setAdapter(applyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Utility.isXposedInstalled(this) || true) {
//            Toast.makeText(this, "add view", Toast.LENGTH_SHORT).show();
//            LinearLayout root  = (LinearLayout) findViewById(R.id.linearLayout_apply_root);
//            CardView cardView = new CardView(this);
//            cardView.setMinimumHeight(100);
//            cardView.setMinimumWidth(100);
//            root.addView(cardView,1);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

}
