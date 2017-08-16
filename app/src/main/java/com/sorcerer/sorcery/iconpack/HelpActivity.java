package com.sorcerer.sorcery.iconpack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity;
import com.sorcerer.sorcery.iconpack.ui.views.AsynMarkdownView;
import com.sorcerer.sorcery.iconpack.utils.NetUtil;

import br.tiagohm.markdownview.css.styles.Github;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/8/15
 */

public class HelpActivity extends BaseSubActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarBackIndicator();

        AsynMarkdownView markdownView = findViewById(R.id.markdownView_help);

        markdownView.addStyleSheet(new Github());

        markdownView.setLoading(true);
        Observable
                .just("https://raw.githubusercontent.com/sorcererXW/SorceryIconPack/master/app/src/main/assets/faq.md")
                .observeOn(Schedulers.io())
                .map(NetUtil::readContent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(markdownView::loadMarkdown, throwable -> {
                    Timber.e(throwable);
                    markdownView.loadMarkdownFromAsset("faq.md");
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_help_markdown;
    }
}