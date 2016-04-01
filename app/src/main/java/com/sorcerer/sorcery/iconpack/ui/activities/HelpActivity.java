package com.sorcerer.sorcery.iconpack.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.HelpAdapter;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;

public class HelpActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_universal);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_help);
        assert recyclerView != null;
        recyclerView.setAdapter(new HelpAdapter(this, recyclerView));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        recyclerView.setHasFixedSize(true);
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
