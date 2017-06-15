package com.sorcerer.sorcery.iconpack.ui.others

import android.view.MotionEvent
import android.view.View

import com.sorcerer.sorcery.iconpack.utils.SimpleCallback

/**
 * @description: 连续多次点击事件的监听器
 * *
 * @author: Sorcerer
 * *
 * @date: 2016/11/12
 */

class OnMultiTouchListener(private val mTime: Int,
                           private val mCallback: SimpleCallback?) : View.OnTouchListener {

    private var mClickTime: Int = 0

    private val mEndAfterFinish = true

    private var mFinished = false

    private var mGap: Long = 500

    private fun setGap(gap: Long) {
        mGap = gap
    }

    private var mLastClickTimeMillis: Long = 0

    init {
        mClickTime = 0
        mLastClickTimeMillis = 0
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (mEndAfterFinish) {
            if (mFinished) {
                return false
            }
        }
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            val now = System.currentTimeMillis()
            if (now - mLastClickTimeMillis <= mGap) {
                mClickTime += 1
            } else {
                mClickTime = 1
            }
            mLastClickTimeMillis = now
            if (mClickTime >= mTime) {
                if (mCallback != null) {
                    mCallback.call()
                    mFinished = true
                }
            }
        }
        return true
    }
}
