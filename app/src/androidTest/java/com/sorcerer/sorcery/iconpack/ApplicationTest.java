package com.sorcerer.sorcery.iconpack;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.sorcerer.sorcery.iconpack.utils.AppInfoUtil;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        System.out.printf(AppInfoUtil.getComponentByName(getContext(), "a10086"));
    }
}