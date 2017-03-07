package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.R;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/5
 */

public class ProgressFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    protected void setChildFragment(Fragment childFragment) {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.relativeLayout_progress_fragment_container,
                        childFragment).commit();
    }
}
