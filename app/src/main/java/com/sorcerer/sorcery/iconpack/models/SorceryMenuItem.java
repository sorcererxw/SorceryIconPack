package com.sorcerer.sorcery.iconpack.models;

/**
 * Created by Sorcerer on 2016/3/23 0023.
 */
public class SorceryMenuItem implements BaseModel {
    private OnSelectListener mOnSelectListener;
    private int mIconRes;
    private String mLabel;

    public interface OnSelectListener {
        void onSelect();
    }

    public SorceryMenuItem(
            OnSelectListener onSelectListener, int iconRes, String label) {
        mOnSelectListener = onSelectListener;
        mIconRes = iconRes;
        mLabel = label;
    }

    public OnSelectListener getOnSelectListener() {
        return mOnSelectListener;
    }

    public void setOnSelectListener(
            OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public void setIconRes(int iconRes) {
        mIconRes = iconRes;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }


}
