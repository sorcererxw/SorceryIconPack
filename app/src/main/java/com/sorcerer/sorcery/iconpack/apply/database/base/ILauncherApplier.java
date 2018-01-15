package com.sorcerer.sorcery.iconpack.apply.database.base;

import java.util.List;

import io.reactivex.Observable;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/2/12
 */

public interface ILauncherApplier {
    Observable<List<String>> restore();

    Observable<List<String>> apply();
}
