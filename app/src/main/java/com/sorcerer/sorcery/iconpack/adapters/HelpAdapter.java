package com.sorcerer.sorcery.iconpack.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;

import org.w3c.dom.Text;

/**
 * Created by Sorcerer on 2016/3/8 0008.
 */
public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    private Context mContext;
    private String[] mStrings;
    private RecyclerView mParent;

    public HelpAdapter(Context context, RecyclerView parent) {
        mContext = context;
        mParent = parent;
        mStrings = mContext.getResources().getStringArray(R.array.help_list);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textView_help_item_title);
            content = (TextView) itemView.findViewById(R.id.textView_help_item_content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_help, mParent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mStrings[position * 2]);
        holder.content.setText(mStrings[position * 2 + 1].replace("|", "\n").replace("#", "    "));
    }

    @Override
    public int getItemCount() {
        return mStrings.length / 2;
    }
}
