package com.sorcerer.sorcery.iconpack;

import com.sorcerer.sorcery.iconpack.apply.database.base.BaseLauncherApplier;
import com.sorcerer.sorcery.iconpack.iconShowCase.overview.IconViewPageAdapter;
import com.sorcerer.sorcery.iconpack.ui.Navigator;
import com.sorcerer.sorcery.iconpack.ui.activities.base.BaseActivity;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.BaseRecyclerViewAdapter;
import com.sorcerer.sorcery.iconpack.ui.adapters.base.BaseRecyclerViewHolder;
import com.sorcerer.sorcery.iconpack.ui.fragments.BaseFragment;
import com.sorcerer.sorcery.iconpack.ui.fragments.BasePreferenceFragmentCompat;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/3/6
 */
@Singleton
@Component(modules = {
        AppModule.class
})
public interface AppComponent {
    void inject(BaseActivity activity);

    void inject(BasePreferenceFragmentCompat fragmentCompat);

    void inject(BaseRecyclerViewHolder viewHolder);

    void inject(BaseFragment fragment);

    void inject(BaseLauncherApplier applier);

    void inject(IconViewPageAdapter adapter);

    void inject(BaseRecyclerViewAdapter.InjectorHelper injectorHelper);

    void inject(Navigator navigator);

}
