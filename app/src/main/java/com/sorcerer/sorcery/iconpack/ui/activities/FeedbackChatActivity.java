package com.sorcerer.sorcery.iconpack.ui.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.feedback.Comment;
import com.avos.avoscloud.feedback.Comment.CommentType;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.mikepenz.materialize.util.KeyboardUtil;
import com.sorcerer.sorcery.iconpack.BuildConfig;
import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.ui.activities.base.UniversalToolbarActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.recyclerviewAdapter.FeedbackChatAdapter;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import rx_activity_result2.RxActivityResult;
import timber.log.Timber;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/10/2
 */

public class FeedbackChatActivity extends UniversalToolbarActivity {
    private FeedbackAgent mFeedbackAgent;

    private FeedbackThread mFeedbackThread;

    private FeedbackThread.SyncCallback mSyncCallback = new FeedbackThread.SyncCallback() {
        private int mLastSize = 0;

        private List<Comment> getNewComments() {
            List<Comment> list = new ArrayList<>();
            for (int i = 0; i < mAdapter.getItemCount() - mLastSize; i++) {
                list.add(mAdapter.getItem(i + mLastSize));
            }
            return list;
        }

        private static final String COMMAND_INFO = "#info";

        private void checkNewComments() {
            List<Comment> list = getNewComments();
            for (Comment comment : list) {
                if (comment == null) {
                    continue;
                }
                if (comment.getCommentType() == CommentType.DEV && comment.getContent()
                        .equals(COMMAND_INFO)) {
                    reportInformation();
                }
            }
        }

        @Override
        public void onCommentsSend(List<Comment> list, AVException e) {
            if (list.size() > mLastSize) {
                mLastSize = list.size();
                mAdapter.notifyDataSetChanged();
                scrollToBottom();
            }
        }

        @Override
        public void onCommentsFetch(List<Comment> list, AVException e) {
            if (list.size() > mLastSize) {
                checkNewComments();
                mLastSize = list.size();
                mAdapter.notifyDataSetChanged();
                scrollToBottom();
            }
        }
    };

    @BindView(R.id.recyclerView_feedback_chat)
    RecyclerView mRecyclerView;

    private FeedbackChatAdapter mAdapter;

    @BindView(R.id.editText_feedback_chat_edit)
    EditText mEditText;

    @BindView(R.id.imageView_feedback_chat_file_button)
    ImageView mFileButton;

    @BindView(R.id.imageView_feedback_chat_send_button)
    ImageView mSendButton;

    @OnClick(R.id.imageView_feedback_chat_file_button)
    void onFileClick() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        Intent takeImageIntent;
                        if (Build.VERSION.SDK_INT < 19) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            takeImageIntent = Intent.createChooser(intent, "select an image");
                        } else {
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            takeImageIntent = intent;
                        }
                        RxActivityResult.on(FeedbackChatActivity.this)
                                .startIntent(takeImageIntent)
                                .subscribe(result -> {
                                    Intent data = result.data();
                                    int resultCode = result.resultCode();

                                    if (resultCode == RESULT_OK) {
                                        result.targetUI().handleImageIntent(data);
                                    }
                                });
                    }
                });
    }

    @OnClick(R.id.imageView_feedback_chat_send_button)
    void onSendClick() {
        send();
    }

    private void handleImageIntent(Intent intent) {
        Uri uri = intent.getData();
        String filePath = getPath(this, uri);
        if (filePath != null) {
            try {
                File file = new File(filePath);
                mFeedbackThread.add(new Comment(file));
                mAdapter.notifyDataSetChanged();
                mFeedbackThread.sync(mSyncCallback);
            } catch (AVException e) {
                Timber.e(e);
            }
        }
    }

    private void send(String s, CommentType type) {
        mFeedbackThread.add(new Comment(s, type));
        mFeedbackThread.sync(mSyncCallback);
    }

    private void send() {
        send(mEditText.getText().toString(), CommentType.USER);
        mEditText.setText("");
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_feedback_chat;
    }

    @Override
    protected void hookBeforeSetContentView() {
        super.hookBeforeSetContentView();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    @Override
    protected void init() {
        super.init();
        setToolbarCloseIndicator();
        mFeedbackAgent = new FeedbackAgent(this);
        mFeedbackThread = mFeedbackAgent.getDefaultThread();
        mAdapter = new FeedbackChatAdapter(this, mFeedbackThread);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnLayoutChangeListener(
                (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                    if (bottom < oldBottom) {
                        mRecyclerView.postDelayed(this::scrollToBottom, 100);
                    }
                });

        mRecyclerView.setOnTouchListener((v, event) -> {
            KeyboardUtil.hideKeyboard(FeedbackChatActivity.this);
            return false;
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            private int mLastLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mLastLength == 0 && s.length() > 0) {
                    showSendButton();
                } else if (mLastLength > 0 && s.length() == 0) {
                    showFileButton();
                }
                mLastLength = s.length();
            }
        });

        scrollToBottom();
        showFileButton();

        mFeedbackThread.setContact(BuildConfig.VERSION_NAME);

        if (mAdapter.getItemCount() == 0) {
            welcome();
        }

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    mFeedbackThread.sync(mSyncCallback);
                });
    }

    private void welcome() {
        new MaterialDialog.Builder(this)
                .title(ResourceUtil.getString(mContext, R.string.feedback_chat_name_dialog_title))
                .content(ResourceUtil
                        .getString(mContext, R.string.feedback_chat_name_dialog_content))
                .input(ResourceUtil.getString(mContext, R.string.feedback_chat_name_dialog_hint),
                        "", false, (dialog, input) -> {

                        })
                .inputRange(3, 20, ResourceUtil.getColor(mContext, R.color.palette_red_500))
                .positiveText(ResourceUtil.getString(mContext, R.string.ok))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        if (dialog.getInputEditText() != null) {
                            final String name = dialog.getInputEditText().getText().toString();
                            if (checkName(name)) {
                                new RxPermissions(FeedbackChatActivity.this)
                                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_PHONE_STATE)
                                        .subscribe(aBoolean -> {
                                            if (aBoolean) {
                                                send(name, CommentType.USER);
                                                send(ResourceUtil.getString(mContext,
                                                        R.string.feedback_chat_welcome),
                                                        CommentType.DEV);
                                                dialog.dismiss();
                                            }
                                        });
                            }
                        }
                    }

                    private boolean checkName(String name) {
                        if (name == null || name.length() < 3) {
                            Toast.makeText(mContext,
                                    ResourceUtil.getString(mContext,
                                            R.string.feedback_chat_name_dialog_toast_hint),
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        return true;
                    }
                })
                .negativeText(ResourceUtil.getString(mContext, R.string.action_leave))
                .onNegative((dialog, which) -> FeedbackChatActivity.this.finish())
                .cancelable(false)
                .autoDismiss(false)
                .canceledOnTouchOutside(false)
                .build()
                .show();
    }

    private void scrollToBottom() {
        mRecyclerView.post(
                () -> mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        if (id == R.id.action_refresh) {
            mFeedbackThread.sync(mSyncCallback);
        }
        if (id == R.id.action_report_info) {
            reportInformation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void reportInformation() {
        String builder = "App Version: " + BuildConfig.VERSION_NAME + "\n" +
                "Device: " + Build.PRODUCT + "\n" +
                "SDK: " + Build.VERSION.SDK_INT + "\n";
        send(builder, CommentType.USER);
    }

    private void showFileButton() {
        showButton(mFileButton, mSendButton);
    }

    private void showSendButton() {
        showButton(mSendButton, mFileButton);
    }

    private void showButton(final View show, final View hide) {
        show.animate().cancel();
        hide.animate().cancel();
        final float defaultAlpha = 0.3f;
        final int animDuration = 200;
        final TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
        hide.animate().alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(interpolator)
                .setDuration(animDuration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hide.setVisibility(GONE);
                        show.animate()
                                .alpha(defaultAlpha)
                                .setDuration(animDuration)
                                .scaleX(1)
                                .scaleY(1)
                                .setInterpolator(interpolator)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        show.setVisibility(VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    private static String getPath(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {
            String docId;
            String[] split;
            String type;
            if (isExternalStorageDocument(uri)) {
                docId = DocumentsContract.getDocumentId(uri);
                split = docId.split(":");
                type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else {
                if (isDownloadsDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    Uri split1 = ContentUris
                            .withAppendedId(Uri.parse("content://downloads/public_downloads"),
                                    Long.valueOf(docId));
                    return getDataColumn(context, split1, null, null);
                }

                if (isMediaDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    split = docId.split(":");
                    type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, "_id=?", selectionArgs);
                }
            }
        } else {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }

            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = new String[]{"_data"};

        String var8;
        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            int column_index = cursor.getColumnIndexOrThrow("_data");
            var8 = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }

        return var8;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void finish() {
        super.finish();
        KeyboardUtil.hideKeyboard(this);
        overridePendingTransition(R.anim.activity_in_scale, R.anim.activity_out_top_to_bottom);
    }
}
