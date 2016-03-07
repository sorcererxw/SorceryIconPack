package com.sorcerer.sorcery.iconpack.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a.a.a.V;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.SIP;
import com.sorcerer.sorcery.iconpack.adapters.LibAdapter;
import com.sorcerer.sorcery.iconpack.adapters.LibListAdapter;
import com.sorcerer.sorcery.iconpack.models.LibraryInfo;
import com.sorcerer.sorcery.iconpack.ui.views.ContributorCard;

import org.w3c.dom.Text;

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

        ContributorCard sorcerer =
                (ContributorCard) findViewById(R.id.contributorCard_sorcerer);
        sorcerer.setAvatarByImageLoader(getResources()
                .getIdentifier("sorcerer_1", "drawable", getPackageName()));
        sorcerer.setName("Sorcerer");
        sorcerer.setJob(
                getString(R.string.job_developer) + " & " + getString(R.string.job_icon_design));
        sorcerer.setGithub(Uri.parse("https://github.com/sorcererxw"));
        sorcerer.setWeibo(Uri.parse("http://weibo.com/u/2262804212"));

        ContributorCard mozartjac = (ContributorCard) findViewById(R.id.contributorCard_mozartjac);
        mozartjac.setAvatarByImageLoader(getResources()
                .getIdentifier("mozartjac", "drawable", getPackageName()));
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
        }
        return false;
    }
}
