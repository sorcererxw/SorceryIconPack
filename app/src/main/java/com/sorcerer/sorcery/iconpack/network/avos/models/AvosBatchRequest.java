package com.sorcerer.sorcery.iconpack.network.avos.models;

import android.support.annotation.Keep;

import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */
@Keep
public class AvosBatchRequest {
    @Keep
    private final List<AvosRequest> requests;

    public AvosBatchRequest(List<AvosRequest> avosRequestList) {
        this.requests = avosRequestList;
    }
}
