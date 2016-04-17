package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sorcerer.sorcery.iconpack.adapters.LibListAdapter;
import com.sorcerer.sorcery.iconpack.models.LibraryInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AboutDialogActivity extends AppCompatActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dialog);

        TextView versionText = (TextView) findViewById(R.id.textView_about_dialog_version);
        assert versionText != null;
        versionText.setText("Version " + BuildConfig.VERSION_NAME);
        versionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLog();
            }
        });

        TextView openSourceText = (TextView) findViewById(R.id.textView_about_dialog_open_source);
        assert openSourceText != null;
        SpannableString openSource = new SpannableString(getString(R.string.open_source_lib));
        openSource.setSpan(new UnderlineSpan(), 0, openSource.length(), 0);
        openSourceText.setText(openSource);
        openSourceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOpenSourceLibs();
            }
        });

        TextView creditText = (TextView) findViewById(R.id.textView_about_dialog_credits);
        StringBuilder htmlBuilder = new StringBuilder("");
        htmlBuilder.append("<a>By</a><br>");
        htmlBuilder.append("<a href=\"https://github.com/sorcererxw\">Sorcerer</a><br>");
        htmlBuilder.append("<a href=\"http://weibo.com/mozartjac\">翟宅宅Jack</a>");
        creditText.setText(Html.fromHtml(htmlBuilder.toString()));
        creditText.setClickable(true);
        creditText.setMovementMethod(LinkMovementMethod.getInstance());

        (findViewById(R.id.relativeLayout_about_dialog_background)).setOnTouchListener(new View
                .OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isPointInsideView(event.getX(),
                        event.getY(),
                        findViewById(R.id.cardView_about_dialog_card))) {
                } else {
                    onBackPressed();
                }
                return false;
            }
        });
    }

    private boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        if ((x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight()))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.fade_out);
    }

    private void showChangeLog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(getString(R.string.changelog));
        builder.content(getString(R.string.changelog_content).replace("|", "\n")
                .replace("#", "" +
                        "    "));
        builder.positiveText(getString(R.string.action_dismiss));
        builder.show();
    }

    private void showOpenSourceLibs() {
        final List<LibraryInfo> list = new ArrayList<LibraryInfo>();
        String[] libs = getResources().getStringArray(R.array.libs_list);
        for (String lib : libs) {
            String[] tmp = lib.split("\\|");
            list.add(new LibraryInfo(tmp[0], tmp[1], tmp[2]));
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        builder.adapter(new LibListAdapter(mContext, list),
                new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which,
                                            CharSequence text) {
                        Intent browserIntent =
                                new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(list.get(which).getLink()));
                        mContext.startActivity(browserIntent);
                    }
                });
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
}
