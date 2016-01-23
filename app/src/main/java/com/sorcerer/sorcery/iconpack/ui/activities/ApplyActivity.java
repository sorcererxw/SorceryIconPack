package com.sorcerer.sorcery.iconpack.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.ApplyAdapter;
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

        final ApplyAdapter applyAdapter = new ApplyAdapter(this, Arrays.asList("Nova", "Go"));
        applyAdapter.setOnItemClickListener(new ApplyAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Utility.applyLauncher(applyAdapter.getLabel(position), view.getContext());
            }
        });
        mApplyRecyclerView.setAdapter(applyAdapter);


    }
}
