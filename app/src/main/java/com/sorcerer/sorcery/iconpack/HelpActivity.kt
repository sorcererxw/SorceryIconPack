package com.sorcerer.sorcery.iconpack

import android.os.Bundle
import android.view.MenuItem
import br.tiagohm.markdownview.css.styles.Github
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseSubActivity
import com.sorcerer.sorcery.iconpack.ui.views.AsynMarkdownView
import com.sorcerer.sorcery.iconpack.utils.NetUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * @description:
 * *
 * @author: Sorcerer
 * *
 * @date: 2017/4/23
 */

class HelpActivity : BaseSubActivity() {

    override fun provideLayoutId(): Int {
        return R.layout.activity_help_markdown
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarBackIndicator()

        val markdownView = findViewById<AsynMarkdownView>(R.id.markdownView_help)

        markdownView.addStyleSheet(Github())

        markdownView.setLoading(true)
        Observable.just("https://raw.githubusercontent.com/wiki/sorcererXW/SorceryIconPack/FAQ.md")
                .observeOn(Schedulers.io())
                .map({ NetUtil.readContent(it) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s -> markdownView.loadMarkdown(s) }, {
                    Timber.e(it)
                    markdownView.loadMarkdownFromAsset("faq.md")
                })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            super.onBackPressed()
        }
        return false
    }
}
