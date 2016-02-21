package com.sorcerer.sorcery.iconpack.models;

/**
 * Created by Sorcerer on 2016/2/15 0015.
 */
public class CheckSettingsItem extends SettingsItem {
    private Boolean mChecked;

    private OnCheckListener mOnCheckListener;

    public interface OnCheckListener {
        void onChecked();

        void onUnchecked();
    }

    public CheckSettingsItem(String title) {
        super(title);
        mChecked = false;
    }

    public CheckSettingsItem(String title, String summary) {
        super(title, summary);
        mChecked = false;
    }

    public CheckSettingsItem(String title, boolean checked) {
        super(title);
        mChecked = checked;
    }

    public CheckSettingsItem(String title, String summary, boolean checked) {
        super(title, summary);
        mChecked = checked;
    }

    public Boolean isChecked() {
        return mChecked;
    }

    public void setChecked(Boolean checked) {
        mChecked = checked;
    }

    public OnCheckListener getOnCheckListener() {
        return mOnCheckListener;
    }

    public void setOnCheckListener(
            OnCheckListener onCheckListener) {
        mOnCheckListener = onCheckListener;
    }

}
