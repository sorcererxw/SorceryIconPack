package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SIP;
import com.sorcerer.sorcery.iconpack.adapters.LibListAdapter;
import com.sorcerer.sorcery.iconpack.models.LibraryInfo;
import com.sorcerer.sorcery.iconpack.ui.activities.base.SlideInAndOutAppCompatActivity;
import com.sorcerer.sorcery.iconpack.ui.views.ContributorCard;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends SlideInAndOutAppCompatActivity {

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        ImageLoader.getInstance().displayImage
                ("drawable://" + getResources()
                                .getIdentifier("sorcery_icon_pack", "drawable", getPackageName()),
                        (ImageView) findViewById(R.id.imageView_about_toolbar), SIP.mOptions);

        ((TextView) findViewById(R.id.textView_about_version_code)).setText(
                "Version: " + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");

        ContributorCard sorcerer =
                (ContributorCard) findViewById(R.id.contributorCard_sorcerer);
        assert sorcerer != null;
        sorcerer.setAvatarByImageLoader(R.drawable.sorcerer_1);
        sorcerer.setName("Sorcerer");
        sorcerer.setJob(
                getString(R.string.job_developer) + " & " + getString(R.string.job_icon_design));
        sorcerer.setGithub(Uri.parse("https://github.com/sorcererxw"));
        sorcerer.setWeibo(Uri.parse("http://weibo.com/u/2262804212"));
        sorcerer.showDivideLine(false);

        ContributorCard mozartjac = (ContributorCard) findViewById(R.id.contributorCard_mozartjac);
        assert mozartjac != null;
        mozartjac.setAvatarByImageLoader(R.drawable.mozartjac);
        mozartjac.setName("翟宅宅Jack");
        mozartjac.setJob(getString(R.string.job_icon_design));
        mozartjac.setWeibo(Uri.parse("http://weibo.com/mozartjac"));

        findViewById(R.id.cardView_about_lib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        } else if (id == R.id.action_about_changelog) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.title(getString(R.string.changelog));
            builder.content(getString(R.string.changelog_content).replace("|", "\n")
                    .replace("#", "" +
                            "    "));
            builder.positiveText(getString(R.string.action_dismiss));
            builder.show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
