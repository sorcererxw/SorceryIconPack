package com.sorcerer.sorcery.iconpack.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.util.Utility;

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

        public View main;
        public TextView title;
        public TextView content;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            main = itemView;
            card = (CardView) itemView.findViewById(R.id.cardView_help_card_item);
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

        if (position == 0) {
            holder.card.setLayoutParams(getItemParams(8,8,8,4));
        } else if (position == getItemCount()) {
            holder.card.setLayoutParams(getItemParams(8,4,8,8));
        } else {
            holder.card.setLayoutParams(getItemParams(8,4,8,4));
        }
    }

    private LinearLayout.LayoutParams getItemParams(int left, int top, int right, int
            bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom));
        return params;
    }

    private int dp2px(int dp) {
        return Utility.dip2px(mContext, dp);
    }

    @Override
    public int getItemCount() {
        return mStrings.length / 2;
    }
}
