package com.sorcerer.sorcery.iconpack.net.avos;

import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/1/27
 */

public class AvosBatchRequest {
    final List<AvosRequest> requests;

    public AvosBatchRequest(List<AvosRequest> avosRequestList) {
        this.requests = avosRequestList;
    }
}
