package com.sorcererxw.rxactivityresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/15
 */

/*
 * HolderActivity, 作为中间的桥梁来传递数据
 */
public class HolderActivity extends Activity {
    private static Request mRequest;

    // 被 A Activity调用, 用于存储回调
    public static void setRequest(Request request) {
        mRequest = request;
    }

    private int mResultCode;
    private Intent mData;
    private OnResult mOnResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 验证回调是否存在
        if (mRequest == null) {
            finish();
            return;
        }

        mOnResult = mRequest.getOnResult();

        //  确保是第一次打开, 防止B Activity被多次打开
        if (savedInstanceState != null) {
            return;
        }

        // 打开B Activity
        startActivityForResult(mRequest.getIntent(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 接收到结果, 直接关闭这个界面
        mResultCode = resultCode;
        mData = data;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 在被结束前执行回调方法
        if (mOnResult != null) {
            mOnResult.response(mResultCode, mData);
        }
    }
}
