package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.ApplyAdapter;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.util.LauncherApplier;
import com.sorcerer.sorcery.iconpack.util.Utility;

public class ApplyActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_universal);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView applyRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_apply);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        applyRecyclerView.setLayoutManager(layoutManager);
        applyRecyclerView.setHasFixedSize(false);

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
        applyRecyclerView.setAdapter(applyAdapter);
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
