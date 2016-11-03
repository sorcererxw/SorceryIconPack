package com.sorcerer.sorcery.iconpack.ui.activities;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.LibraryInfo;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.LibListAdapter;
import com.sorcerer.sorcery.iconpack.util.ResourceUtil;
import com.sorcerer.sorcery.iconpack.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutDialogActivity extends BaseActivity {

    @BindView(R.id.textView_about_dialog_version)
    TextView mVersionTextView;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.fast_fade_out);
    }

    @OnClick(R.id.textView_about_dialog_version)
    void showChangeLog() {

    }

    @BindView(R.id.textView_about_dialog_open_source)
    TextView mOpenSourceTextView;

    @OnClick(R.id.textView_about_dialog_open_source)
    void showOpenSourceLibs() {
        final List<LibraryInfo> list = new ArrayList<>();
        String[] libs = getResources().getStringArray(R.array.libs_list);
        for (String lib : libs) {
            String[] tmp = lib.split("\\|");
            list.add(new LibraryInfo(tmp[0], tmp[1], tmp[2]));
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        builder.adapter(new LibListAdapter(mContext, list),
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        builder.title(getString(R.string.open_source_lib));
        builder.positiveText(getString(R.string.action_close));
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog,
                                @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @BindView(R.id.textView_about_dialog_credits)
    TextView mCreditsTextView;

    @BindView(R.id.relativeLayout_about_dialog_background)
    View mBackground;

    @BindView(R.id.cardView_about_dialog_card)
    View mCard;

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_about_dialog;
    }

    @Override
    protected void init() {
        mVersionTextView
                .setText(String.format("%s: %s", ResourceUtil.getString(this, R.string.version),
                        BuildConfig.VERSION_NAME));

        SpannableString openSource = new SpannableString(getString(R.string.open_source_lib));
        openSource.setSpan(new UnderlineSpan(), 0, openSource.length(), 0);
        mOpenSourceTextView.setText(openSource);

        String htmlBuilder = "";
        htmlBuilder += ("<a>" + ResourceUtil.getString(this, R.string.contributor) + "</a><br>");
        htmlBuilder += ("<a href=\"https://github.com/sorcererxw\">Sorcerer</a><br>");
        htmlBuilder += ("<a href=\"http://weibo.com/mozartjac\">翟宅宅Jack</a><br>");
        htmlBuilder += ("<a>nako liu</a>");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCreditsTextView.setText(Html.fromHtml(htmlBuilder, Html.FROM_HTML_MODE_LEGACY));
        } else {
            mCreditsTextView.setText(Html.fromHtml(htmlBuilder));
        }


        mCreditsTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mBackground.setOnTouchListener(new View
                .OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!ViewUtil.isPointInsideView(event.getX(), event.getY(), mCard)) {
                    onBackPressed();
                }
                return false;
            }
        });
    }

}