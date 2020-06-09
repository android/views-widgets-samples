package com.google.androidstudio.motionlayoutexample.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.NestedScrollingChild2
import com.google.androidstudio.motionlayoutexample.R

class TestMotionLayout : MotionLayout, NestedScrollingChild2 {

    constructor(context: Context) :
            super(context) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onFilterTouchEventForSecurity(event: MotionEvent?): Boolean {
        return super.onFilterTouchEventForSecurity(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(event)
    }


    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return super.onStartNestedScroll(child, target, axes, type)
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        super.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun getNestedScrollAxes(): Int {
        return super.getNestedScrollAxes()
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun stopNestedScroll(type: Int) {
        TODO("Not yet implemented")
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        TODO("Not yet implemented")
    }
}