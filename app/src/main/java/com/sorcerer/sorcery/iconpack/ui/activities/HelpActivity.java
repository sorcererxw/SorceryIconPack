package com.sorcerer.sorcery.iconpack.ui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.HelpAdapter;
import com.sorcerer.sorcery.iconpack.databinding.ActivityHelpBinding;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;

public class HelpActivity extends SlideInAndOutAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_help);

        setSupportActionBar(binding.include4.toolbarUniversal);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = binding.recyclerViewHelp;
        binding.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));

        recyclerView.setAdapter(new HelpAdapter(this, recyclerView));
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
