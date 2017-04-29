package com.sorcerer.sorcery.iconpack.ui.views.markdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.sorcerer.sorcery.iconpack.R;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.AutoLink;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.abbreviation.Abbreviation;
import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.superscript.SuperscriptExtension;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.Utils;
import br.tiagohm.markdownview.css.ExternalStyleSheet;
import br.tiagohm.markdownview.css.StyleSheet;
import br.tiagohm.markdownview.ext.button.ButtonExtension;
import br.tiagohm.markdownview.ext.emoji.EmojiExtension;
import br.tiagohm.markdownview.ext.kbd.Keystroke;
import br.tiagohm.markdownview.ext.kbd.KeystrokeExtension;
import br.tiagohm.markdownview.ext.label.LabelExtension;
import br.tiagohm.markdownview.ext.localization.LocalizationExtension;
import br.tiagohm.markdownview.ext.mark.Mark;
import br.tiagohm.markdownview.ext.mark.MarkExtension;
import br.tiagohm.markdownview.ext.mathjax.MathJax;
import br.tiagohm.markdownview.ext.mathjax.MathJaxExtension;
import br.tiagohm.markdownview.ext.twitter.TwitterExtension;
import br.tiagohm.markdownview.ext.video.VideoLinkExtension;
import br.tiagohm.markdownview.js.ExternalScript;
import br.tiagohm.markdownview.js.JavaScript;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/4/27
 */

@SuppressWarnings("unused")
public class AsynMarkdownView extends RelativeLayout {
    public static final JavaScript JQUERY_3 =
            new ExternalScript("file:///android_asset/js/jquery-3.1.1.min.js", false, false);
    public static final JavaScript HIGHLIGHTJS =
            new ExternalScript("file:///android_asset/js/highlight.js", false, true);
    public static final JavaScript MATHJAX = new ExternalScript(
            "https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS_CHTML", false, true);
    public static final JavaScript HIGHLIGHT_INIT =
            new ExternalScript("file:///android_asset/js/highlight-init.js", false, true);
    public static final JavaScript MATHJAX_CONFIG =
            new ExternalScript("file:///android_asset/js/mathjax-config.js", false, true);
    public static final JavaScript TOOLTIPSTER_JS =
            new ExternalScript("file:///android_asset/js/tooltipster.bundle.min.js", false, true);
    public static final JavaScript TOOLTIPSTER_INIT =
            new ExternalScript("file:///android_asset/js/tooltipster-init.js", false, true);
    public static final StyleSheet TOOLTIPSTER_CSS =
            new ExternalStyleSheet("file:///android_asset/css/tooltipster.bundle.min.css");
    private static final List<Extension> EXTENSIONS =
            Arrays.asList(TablesExtension.create(), TaskListExtension.create(),
                    AbbreviationExtension.create(), AutolinkExtension.create(),
                    MarkExtension.create(), StrikethroughSubscriptExtension.create(),
                    SuperscriptExtension.create(), KeystrokeExtension.create(),
                    MathJaxExtension.create(), FootnoteExtension.create(), EmojiExtension.create(),
                    VideoLinkExtension.create(), TwitterExtension.create(), LabelExtension.create(),
                    ButtonExtension.create(), LocalizationExtension.create());
    private final DataHolder OPTIONS;
    private final List<StyleSheet> mStyleSheets;
    private final HashSet<JavaScript> mScripts;
    private WebView mWebView;
    private boolean mEscapeHtml;
    private MarkdownView.OnElementListener mOnElementListener;
    private ProgressBar mProgressBar;

    public AsynMarkdownView(Context context) {
        this(context, null);
    }

    public AsynMarkdownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsynMarkdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        OPTIONS = new MutableDataSet().set(FootnoteExtension.FOOTNOTE_REF_PREFIX, "[")
                .set(FootnoteExtension.FOOTNOTE_REF_SUFFIX, "]").set(
                        HtmlRenderer.FENCED_CODE_LANGUAGE_CLASS_PREFIX, "")
                .set(HtmlRenderer.FENCED_CODE_NO_LANGUAGE_CLASS, "nohighlight");
        mStyleSheets = new LinkedList<>();
        mScripts = new LinkedHashSet<>();
        mEscapeHtml = true;
        mWebView = new WebView(context, null, 0);
        mWebView.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        ((MutableDataHolder) OPTIONS).set(LocalizationExtension.LOCALIZATION_CONTEXT, context);

        try {
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.addJavascriptInterface(new EventDispatcher(), "android");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TypedArray e = getContext().obtainStyledAttributes(attrs, R.styleable.MarkdownView);
            mEscapeHtml = e.getBoolean(R.styleable.MarkdownView_escapeHtml, true);
            e.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addView(mWebView);
        addJavascript(JQUERY_3);

        mProgressBar = new ProgressBar(context);
        LayoutParams ptogressBarLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        ptogressBarLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(ptogressBarLayoutParams);
        mProgressBar.setScrollBarStyle(SCROLLBARS_INSIDE_INSET);
        mProgressBar.setVisibility(INVISIBLE);
        addView(mProgressBar);
    }

    public void setOnElementListener(MarkdownView.OnElementListener listener) {
        mOnElementListener = listener;
    }

    public AsynMarkdownView setEscapeHtml(boolean flag) {
        mEscapeHtml = flag;
        return this;
    }

    public AsynMarkdownView setEmojiRootPath(String path) {
        ((MutableDataHolder) OPTIONS).set(EmojiExtension.ROOT_IMAGE_PATH, path);
        return this;
    }

    public AsynMarkdownView setEmojiImageExtension(String ext) {
        ((MutableDataHolder) OPTIONS).set(EmojiExtension.IMAGE_EXT, ext);
        return this;
    }

    public AsynMarkdownView addStyleSheet(StyleSheet s) {
        if (s != null && !mStyleSheets.contains(s)) {
            mStyleSheets.add(s);
        }

        return this;
    }

    public AsynMarkdownView replaceStyleSheet(StyleSheet oldStyle, StyleSheet newStyle) {
        if (oldStyle != newStyle) {
            if (newStyle == null) {
                mStyleSheets.remove(oldStyle);
            } else {
                int index = mStyleSheets.indexOf(oldStyle);
                if (index >= 0) {
                    mStyleSheets.set(index, newStyle);
                } else {
                    addStyleSheet(newStyle);
                }
            }
        }

        return this;
    }

    public AsynMarkdownView removeStyleSheet(StyleSheet s) {
        mStyleSheets.remove(s);
        return this;
    }

    public AsynMarkdownView addJavascript(JavaScript js) {
        mScripts.add(js);
        return this;
    }

    public AsynMarkdownView removeJavaScript(JavaScript js) {
        mScripts.remove(js);
        return this;
    }

    private String parseBuildAndRender(String text) {
        Parser parser = Parser.builder(OPTIONS).extensions(EXTENSIONS).build();
        HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS).escapeHtml(mEscapeHtml)
                .attributeProviderFactory(new IndependentAttributeProviderFactory() {
                    public AttributeProvider create(NodeRendererContext context) {
                        return new CustomAttributeProvider();
                    }
                }).nodeRendererFactory(
                        new MarkdownView.NodeRendererFactoryImpl())
                .extensions(EXTENSIONS).build();
        return renderer.render(parser.parse(text));
    }

    public void loadMarkdown(String markdown) {
        Observable.just(markdown)
                .observeOn(Schedulers.computation())
                .map(text -> {
                    String html = parseBuildAndRender(text);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>\n");
                    sb.append("<head>\n");
                    Iterator var4 = mStyleSheets.iterator();

                    while (var4.hasNext()) {
                        StyleSheet js = (StyleSheet) var4.next();
                        sb.append(js.toHTML());
                    }

                    var4 = mScripts.iterator();

                    while (var4.hasNext()) {
                        JavaScript js1 = (JavaScript) var4.next();
                        sb.append(js1.toHTML());
                    }

                    sb.append("</head>\n");
                    sb.append("<body>\n");
                    sb.append("<div class=\"container\">\n");
                    sb.append(html);
                    sb.append("</div>\n");
                    sb.append("</body>\n");
                    sb.append("</html>");
                    html = sb.toString();
                    Logger.d(html);
                    return html;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(html -> {
                    mWebView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                    mProgressBar.setVisibility(INVISIBLE);
                });
    }

    public void loadMarkdownFromAsset(String path) {
        Observable.just(path)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> {
                    mProgressBar.setVisibility(VISIBLE);
                })
                .observeOn(Schedulers.io())
                .map(s -> Utils.getStringFromAssetFile(getContext().getAssets(), path))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadMarkdown);
    }

    public void loadMarkdownFromFile(File file) {
        Observable.just(file)
                .observeOn(Schedulers.io())
                .map(Utils::getStringFromFile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadMarkdown);
    }

    public void loadMarkdownFromUrl(String url) {
        Observable.just(url)
                .observeOn(Schedulers.io())
                .map(url1 -> {
                    InputStream is = null;

                    String var5;
                    try {
                        URLConnection e = (new URL(url1)).openConnection();
                        e.setReadTimeout(5000);
                        e.setConnectTimeout(5000);
                        e.setRequestProperty("Accept-Charset", "UTF-8");
                        var5 = Utils.getStringFromInputStream(is = e.getInputStream());
                        return var5;
                    } catch (Exception var15) {
                        var15.printStackTrace();
                        var5 = "";
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException var14) {
                                var14.printStackTrace();
                            }
                        }
                    }

                    return var5;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadMarkdown);
    }

    protected class EventDispatcher {
        protected EventDispatcher() {
        }

        @JavascriptInterface
        public void onButtonTap(String id) {
            if (mOnElementListener != null) {
                mOnElementListener.onButtonTap(id);
            }

        }

        @JavascriptInterface
        public void onCodeTap(String lang, String code) {
            if (mOnElementListener != null) {
                mOnElementListener.onCodeTap(lang, code);
            }

        }

        @JavascriptInterface
        public void onHeadingTap(int level, String text) {
            if (mOnElementListener != null) {
                mOnElementListener
                        .onHeadingTap(level, text);
            }

        }

        @JavascriptInterface
        public void onImageTap(String src, int width, int height) {
            if (mOnElementListener != null) {
                mOnElementListener.onImageTap(src, width, height);
            }

        }

        @JavascriptInterface
        public void onMarkTap(String text) {
            if (mOnElementListener != null) {
                mOnElementListener.onMarkTap(text);
            }

        }

        @JavascriptInterface
        public void onKeystrokeTap(String key) {
            if (mOnElementListener != null) {
                mOnElementListener.onKeystrokeTap(key);
            }

        }

        @JavascriptInterface
        public void onLinkTap(String href, String text) {
            if (mOnElementListener != null) {
                mOnElementListener.onLinkTap(href, text);
            }

        }
    }

    public class CustomAttributeProvider implements AttributeProvider {
        public CustomAttributeProvider() {
        }

        public void setAttributes(Node node, AttributablePart part, Attributes attributes) {
            if (node instanceof FencedCodeBlock) {
                if (part.getName().equals("NODE")) {
                    String language = ((FencedCodeBlock) node).getInfo().toString();
                    if (!TextUtils.isEmpty(language) && !language.equals("nohighlight")) {
                        addJavascript(
                                MarkdownView.HIGHLIGHTJS);
                        addJavascript(
                                MarkdownView.HIGHLIGHT_INIT);
                        attributes.addValue("language", language);
                        attributes.addValue("onclick", String.format(
                                "javascript:android.onCodeTap(\'%s\', this.textContent);",
                                language));
                    }
                }
            } else if (node instanceof MathJax) {
                addJavascript(MarkdownView.MATHJAX);
                addJavascript(
                        MarkdownView.MATHJAX_CONFIG);
            } else if (node instanceof Abbreviation) {
                addJavascript(
                        MarkdownView.TOOLTIPSTER_JS);
                addStyleSheet(
                        MarkdownView.TOOLTIPSTER_CSS);
                addJavascript(
                        MarkdownView.TOOLTIPSTER_INIT);
                attributes.addValue("class", "tooltip");
            } else if (node instanceof Heading) {
                attributes.addValue("onclick",
                        String.format(Locale.getDefault(),
                                "javascript:android.onHeadingTap(%d, \'%s\');",
                                ((Heading) node).getLevel(),
                                ((Heading) node).getText()));
            } else if (node instanceof Image) {
                attributes.addValue("onclick",
                        "javascript: android.onImageTap(this.src, this.clientWidth, this.clientHeight);");
            } else if (node instanceof Mark) {
                attributes.addValue("onclick", "javascript: android.onMarkTap(this.textContent)");
            } else if (node instanceof Keystroke) {
                attributes.addValue("onclick",
                        "javascript: android.onKeystrokeTap(this.textContent)");
            } else if (node instanceof Link || node instanceof AutoLink) {
                attributes.addValue("onclick",
                        "javascript: android.onLinkTap(this.href, this.textContent)");
            }

        }
    }

}
