package com.sorcerer.sorcery.iconpack.feedback.chat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.feedback.Comment;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.bumptech.glide.Glide;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.PhotoViewActivity;
import com.sorcerer.sorcery.iconpack.utils.DisplayUtil;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.mthli.slice.Slice;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/2
 */

public class FeedbackChatAdapter
        extends RecyclerView.Adapter<FeedbackChatAdapter.FeedbackViewHolder> {

    static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linearLayout_item_feedback_frame)
        LinearLayout mFrame;

        @BindView(R.id.linearLayout_item_feedback_container)
        LinearLayout mContainer;

        @BindView(R.id.imageView_item_feedback_chat)
        ImageView mImageView;

        @BindView(R.id.textView_item_feedback_chat)
        TextView mTextView;

        FeedbackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context mContext;

    private FeedbackThread mFeedbackThread;

    public FeedbackChatAdapter(Context context, FeedbackThread feedbackThread) {
        mContext = context;
        mFeedbackThread = feedbackThread;
    }

    private static final int TYPE_USER = 0x01;
    private static final int TYPE_DEV = 0x02;

    public Comment getItem(int position) {
        return mFeedbackThread.getCommentsList().get(position);
    }

    @Override
    public int getItemViewType(int position) {
        Comment comment = getItem(position);
        if (comment.getCommentType() == Comment.CommentType.DEV) {
            return TYPE_DEV;
        } else {
            return TYPE_USER;
        }
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedbackViewHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.item_feedback_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(final FeedbackViewHolder holder, int position) {
        int type = getItemViewType(position);

        Slice slice = new Slice(holder.mFrame);
        slice.setRadius(10.0f);

        if (type == TYPE_DEV) {
            holder.mContainer.setGravity(Gravity.START);
        } else {
            holder.mContainer.setGravity(Gravity.END);
        }
        if (type == TYPE_DEV) {
            slice.setColor(ResourceUtil.getColor(mContext, R.color.palette_grey_100));
        } else {
            slice.setColor(ResourceUtil.getColor(mContext, R.color.palette_green_100));
        }

        int top, bottom, left, right;
        int elevation;
        if (position != 0 && getItemViewType(position) == getItemViewType(position - 1)) {
            // no top radius
            if (type == TYPE_DEV) {
                slice.showRightTopRect(false);
                slice.showLeftTopRect(true);
            } else {
                slice.showRightTopRect(true);
                slice.showLeftTopRect(false);
            }

            top = 1;
        } else {
            // has top radius
            slice.showLeftTopRect(false);
            slice.showRightTopRect(false);

            top = 8;
        }
        if (position != getItemCount() - 1
                && getItemViewType(position) == getItemViewType(position + 1)) {
            // no bottom radius
            if (type == TYPE_DEV) {
                slice.showRightBottomRect(false);
                slice.showLeftBottomRect(true);
            } else {
                slice.showRightBottomRect(true);
                slice.showLeftBottomRect(false);
            }

            bottom = 1;
            elevation = 1;
        } else {
            // has bottom radius
            slice.showRightBottomRect(false);
            slice.showLeftBottomRect(false);

            bottom = 8;
            elevation = 2;
        }
        slice.setElevation(elevation);

        if (type == TYPE_DEV) {
            left = 24;
            right = 64;
        } else {
            left = 64;
            right = 24;
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(
                DisplayUtil.dip2px(mContext, left),
                DisplayUtil.dip2px(mContext, top),
                DisplayUtil.dip2px(mContext, right),
                DisplayUtil.dip2px(mContext, bottom));
        holder.mFrame.setLayoutParams(layoutParams);

        final Comment comment = getItem(position);

        if (comment.getContent() != null && !comment.getContent().isEmpty()) {
            holder.mTextView.setVisibility(View.VISIBLE);
            holder.mTextView.setText(comment.getContent());
            holder.mFrame.setOnLongClickListener(v -> {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
                builder.title(
                        mContext.getString(R.string.action_copy_to_clipboard) + " ?");
                builder.positiveText(mContext.getString(R.string.action_copy));
                builder.negativeText(mContext.getString(R.string.cancel));
                builder.onPositive((dialog, which) -> {
                    ClipboardManager clipboard = (ClipboardManager) mContext
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip =
                            ClipData.newPlainText("feedback chat", comment.getContent());
                    clipboard.setPrimaryClip(clip);
                });
                builder.show();
                return false;
            });
        } else {
            holder.mTextView.setVisibility(View.GONE);
        }

        if (comment.getAttachment() != null && comment.getAttachment().getUrl() != null) {
            holder.mImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(comment.getAttachment().getUrl())
                    .placeholder(
                            new ColorDrawable(
                                    ResourceUtil.getColor(mContext, R.color.palette_grey_400)))
                    .into(holder.mImageView);
            holder.mImageView.setOnClickListener(v -> PhotoViewActivity
                    .startActivity(mContext, comment.getAttachment().getUrl()));
        } else {
            holder.mImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mFeedbackThread.getCommentsList().size();
    }
}
