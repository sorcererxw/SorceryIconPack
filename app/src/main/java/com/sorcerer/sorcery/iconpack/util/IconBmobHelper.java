package com.sorcerer.sorcery.iconpack.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.sorcerer.sorcery.iconpack.models.IconBmob;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Sorcerer on 2016/2/29 0029.
 */
public class IconBmobHelper {

    private static final String TAG = "IconBmobHelper";

    public static final String PREF_NAME = "ICON_BMOB";
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public IconBmobHelper(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void like(final String name, boolean like) {
        String id = mSharedPreferences.getString(name + "_ID", "# #");
        Log.d(TAG, name + "\n" + id);
        if (id.equals("# #")) {
            final IconBmob icon = new IconBmob(mContext);
            icon.setName(name);
            icon.setLike(like);
            icon.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    mSharedPreferences.edit().putString(name + "_ID", icon.getObjectId()).apply();
                    Log.d(TAG, "apply " + name + "_ID");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d(TAG, i + " " + s);
                }
            });
        } else {
            final IconBmob icon = new IconBmob();
            icon.setObjectId(id);
            icon.setLike(like);
//            icon.setName(name);
            icon.update(mContext, id, new UpdateListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int i, String s) {
                }
            });
        }
    }
}
