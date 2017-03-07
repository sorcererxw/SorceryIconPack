package com.sorcerer.sorcery.iconpack.network.avos.models;

import android.support.annotation.Keep;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/3
 */
@Keep
public class AvosQuerySelection {
    private Map<String, Object> where = new HashMap<>();

    public AvosQuerySelection() {

    }

    public void setSelection(String key, Object value) {
        where.put(key, value);
    }

    @Override
    public String toString() {
        return new Gson().toJson(where);
    }
}


