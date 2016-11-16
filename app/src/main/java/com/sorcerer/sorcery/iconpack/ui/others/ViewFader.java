package com.sorcerer.sorcery.iconpack.ui.others;

import android.view.View;
import android.view.ViewGroup;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

public class ViewFader {
    public void hideContentOf(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setVisibility(View.GONE);
        }
    }

    public void showContent(ViewGroup viewGroup){
        for(int i=0;i<viewGroup.getChildCount();i++){
            viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }
}
