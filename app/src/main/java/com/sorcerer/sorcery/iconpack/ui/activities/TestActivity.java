package com.sorcerer.sorcery.iconpack.ui.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.adapters.LibAdapter;
import com.sorcerer.sorcery.iconpack.adapters.LibListAdapter;
import com.sorcerer.sorcery.iconpack.models.CheckSettingsItem;
import com.sorcerer.sorcery.iconpack.models.ComponentBean;
import com.sorcerer.sorcery.iconpack.util.Utility;
import com.sorcerer.sorcery.iconpack.xposed.XposedUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends SlideInAndOutAppCompatActivity {

    private Context mContext;
    private Button mTestButton;
    private ImageView mImageView;
    private TextView mTestTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mTestButton = (Button) findViewById(R.id.button_test);
        mTestTextView = (TextView) findViewById(R.id.textView_test);
        mImageView = (ImageView) findViewById(R.id.imageView_test);
        mContext = this;

        mTestButton.setOnClickListener(xmlListener);
    }

//    private View.OnClickListener showListDialog = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            View view = LinearLayout.inflate(mContext, R.layout.layout_recyclerview, null);
//            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id
//                    .recyclerView_layout);
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//            recyclerView.setAdapter(new LibAdapter(mContext));
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setView(view);
//            builder.create().show();
//
//
////            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
//
////            builder.customView(view, true);
////            builder.adapter(new LibListAdapter(mContext), null);
////            builder.title(getString(R.string.open_source_lib));
////            builder.build().show();
//        }
//
//    };

    private View.OnClickListener getXmlStringListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            try {
//                XmlResourceParser xml = getResources().getXml(R.xml.appfilter);
//                String xmlString = Utility.convertStreamToString(inputStream);

//                mTestTextView.setText(xmlString);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            mTestTextView.setText(Utility.getAppfilterToString(mContext));
        }
    };

    private View.OnClickListener killListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityManager manager = (ActivityManager) getSystemService(Context
                    .ACTIVITY_SERVICE);
            XposedUtils.killAll(getPackageManager(), manager);
        }
    };

    private View.OnClickListener bitmapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            File file = new File("/data/data/com.sorcerer.sorcery.iconpack/cache/icons/com.google" +
                    ".android.apps.paidtasks_drawable_google_opinion_rewards");
            Bitmap bitmap = BitmapFactory
                    .decodeFile("/data/data/com.sorcerer.sorcery.iconpack/cache/icons/" +
                            ((EditText) findViewById(R.id.editText_test)).getText().toString());
            mImageView.setImageBitmap(bitmap);
        }
    };


    private View.OnClickListener xmlListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = getResources().getIdentifier("appfilter", "xml", getPackageName());
            XmlResourceParser parser = getResources().getXml(i);
            int eventType = -1;
            List<ComponentBean> list = new ArrayList<ComponentBean>();
            try {
                eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("calendar")) {
//                                    ComponentBean bean = new ComponentBean();
//                                    bean.setComponent(parser.getAttributeValue(0));
//                                    bean.setPrefix(parser.getAttributePrefix(1));
//                                    bean.setCalendar(true);
//                                    list.add(bean);
                            } else if (parser.getName().equals("item")) {
                                ComponentBean bean = new ComponentBean();
                                bean.setComponent(parser.getAttributeValue(0));
                                bean.setDrawable(parser.getAttributeValue(1));
                                bean.setCalendar(false);
                                list.add(bean);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String s = "";
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).isCalendar()) {
                    s += "calendar\n";
                    s += list.get(j).getPrefix() + "\n";
                } else {
                    s += "item\n";
                    s += list.get(j).getDrawable() + "\n";
                }
                s += list.get(j).getComponent() + "\n\n";
            }
            mTestTextView.setText(s);

        }
    };
}
