package com.sorcerer.sorcery.iconpack;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import butterknife.BindView;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/4/23
 */

public class HelpActivity extends BaseSubActivity {
    @BindView(R.id.coordinatorLayout_help)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.markdownView_help)
    MarkdownView mMarkdownView;

    @Override
    protected ViewGroup rootView() {
        return mCoordinatorLayout;
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_help_markdown;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setToolbarBackIndicator();

        mMarkdownView.addStyleSheet(new Github());
        mMarkdownView.loadMarkdownFromAsset("faq.md");
//        mMarkdownView.loadMarkdownFromUrl(
//                "https://raw.githubusercontent.com/wiki/sorcererXW/SorceryIconPack/FAQ.md");
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
