package com.sorcerer.sorcery.iconpack.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sorcerer on 2016/2/25 0025.
 */
public class LibAdapter extends RecyclerView.Adapter<LibAdapter.LibHolder> {

    private static final String TAG = "SIP/LibAdapter";
    private Context mContext;
    private List<LibInfo> mLibList;

    public LibAdapter(Context context) {
        mContext = context;
        mLibList = new ArrayList();
        String[] libs = context.getResources().getStringArray(R.array.libs_list);
        for (String lib : libs) {
            String[] tmp = lib.split("\\|");
            mLibList.add(new LibInfo(tmp[0], tmp[1], tmp[2]));
            Log.d(TAG, tmp[0] + " " + tmp[1] + " " + tmp[2]);
        }
    }

    class LibHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView author;
        View main;

        public LibHolder(View itemView) {
            super(itemView);
            main = itemView;
            name = (TextView) itemView.findViewById(R.id.textView_lib_item_name);
            author = (TextView) itemView.findViewById(R.id.textView_lib_item_author);
        }
    }

    class LibInfo {
        String name;
        String author;
        String link;

        public LibInfo(String name, String author, String link) {
            this.name = name;
            this.author = author;
            this.link = link;
        }
    }


    @Override
    public LibHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LibHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.item_lib, parent, false));
    }


    @Override
    public void onBindViewHolder(LibHolder holder, final int position) {
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse(mLibList.get(position).link));
                mContext.startActivity(browserIntent);
            }
        });
        holder.name.setText(mLibList.get(position).name);
        holder.author.setText(mLibList.get(position).author);
    }

    @Override
    public int getItemCount() {
        return mLibList.size();
    }
}
