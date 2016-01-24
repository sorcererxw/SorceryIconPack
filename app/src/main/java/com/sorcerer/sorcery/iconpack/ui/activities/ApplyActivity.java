package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.ApplyAdapter;
import com.sorcerer.sorcery.iconpack.util.LauncherApplier;
import com.sorcerer.sorcery.iconpack.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;

public class ApplyActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mApplyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_apply);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mApplyRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_apply);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        mApplyRecyclerView.setLayoutManager(layoutManager);
        mApplyRecyclerView.setHasFixedSize(true);

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
                    final String appPackageName = applyAdapter.getItem(position).getPackageName(); //
                    // getPackageName()
                    // from Context
                    // or Activity
                    // object
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

}
