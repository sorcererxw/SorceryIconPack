package com.sorcerer.sorcery.iconpack.settings.licenses.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorcerer.sorcery.iconpack.R;
import com.sorcerer.sorcery.iconpack.settings.licenses.models.OpenSourceLibBean;
import com.sorcerer.sorcery.iconpack.settings.licenses.models.OpenSourceLibInformation;
import com.sorcerer.sorcery.iconpack.utils.ResourceUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/27
 */

public class OpenSourceLibFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<OpenSourceLibBean> mOpenSourceLibBeanList = Arrays.asList(
            new OpenSourceLibInformation.RxJavaInfoBean(),
            new OpenSourceLibInformation.RxAndroidInfoBean(),
            new OpenSourceLibInformation.RxActivityResultInfoBean(),
            new OpenSourceLibInformation.RxPermissionInfoBean(),
            new OpenSourceLibInformation.XposedBridgeInfoBean(),
            new OpenSourceLibInformation.GlideInfoBean(),
            new OpenSourceLibInformation.MaterialDrawerInfoBean(),
            new OpenSourceLibInformation.MaterialDialogsInfoBean(),
            new OpenSourceLibInformation.SliceInfoBean(),
            new OpenSourceLibInformation.AVLoadingIndicatorViewInfoBean(),
            new OpenSourceLibInformation.PhotoViewInfoBean(),
            new OpenSourceLibInformation.IconicsInfoBean(),
            new OpenSourceLibInformation.ButterknifeInfoBean(),
            new OpenSourceLibInformation.RetrofitInfoBean(),
            new OpenSourceLibInformation.LibsuperuserInfoBean(),
            new OpenSourceLibInformation.GsonInfoBean(),
            new OpenSourceLibInformation.JsoupInfoBean(),
            new OpenSourceLibInformation.LightweightStreamApiInfoBean(),
            new OpenSourceLibInformation.MaterializeInfoBean()
    );

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opensourcelib, container, false);
        ButterKnife.bind(this, view);

        OpenSourceLibAdapter adapter =
                new OpenSourceLibAdapter(getContext(), mOpenSourceLibBeanList);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(ResourceUtil.getString(getContext(), R.string.preference_about_lib));
    }

}
