package com.sorcerer.sorcery.iconpack.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.LibraryInfo;

import java.util.List;

/**
 * Created by Sorcerer on 2016/2/25 0025.
 */
public class LibListAdapter extends BaseAdapter {

    private Context mContext;
    private List<LibraryInfo> mLibList;

    public LibListAdapter(Context context, List<LibraryInfo> libList) {
        mContext = context;
        mLibList = libList;
    }


    @Override
    public int getCount() {
        return mLibList.size();
    }


    @Override
    public Object getItem(int position) {
        return mLibList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LinearLayout.inflate(mContext, R.layout.item_lib, null);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent browserIntent =
//                        new Intent(Intent.ACTION_VIEW, Uri.parse(mLibList.get(position).link));
//                mContext.startActivity(browserIntent);
//            }
//        });
        ((TextView) convertView.findViewById(R.id.textView_lib_item_name))
                .setText(mLibList.get(position).getName());
        ((TextView) convertView.findViewById(R.id.textView_lib_item_author))
                .setText(mLibList.get(position).getDeveloper());
        return convertView;
    }
}
