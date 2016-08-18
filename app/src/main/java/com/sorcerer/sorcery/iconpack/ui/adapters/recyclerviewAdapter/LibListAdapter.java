package com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.models.LibraryInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/2/25 0025.
 */
public class LibListAdapter extends RecyclerView.Adapter<LibListAdapter.LibViewHolder> {

    private Context mContext;
    private List<LibraryInfo> mLibList;

    class LibViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_lib_item_name)
        TextView name;

        @BindView(R.id.textView_lib_item_author)
        TextView author;

        public LibViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public LibListAdapter(Context context, List<LibraryInfo> libList) {
        mContext = context;
        mLibList = libList;
    }

    @Override
    public LibViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LibViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_lib, parent, false));
    }

    @Override
    public void onBindViewHolder(LibViewHolder holder, final int position) {
        holder.name.setText(mLibList.get(position).getName());
        holder.author.setText(mLibList.get(position).getDeveloper());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(mLibList.get(position).getLink()));
                mContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLibList.size();
    }
}
