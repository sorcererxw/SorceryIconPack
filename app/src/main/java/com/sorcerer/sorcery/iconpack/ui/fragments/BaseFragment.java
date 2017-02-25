package com.sorcerer.sorcery.iconpack.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/24
 */

public class BaseFragment extends Fragment {

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
