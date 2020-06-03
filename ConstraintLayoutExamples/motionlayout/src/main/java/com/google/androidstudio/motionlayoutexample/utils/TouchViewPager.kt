package com.google.androidstudio.motionlayoutexample.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.NestedScrollingParent2
import androidx.viewpager.widget.ViewPager

class TouchViewPager : ViewPager, NestedScrollingParent2 {

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    private fun initialize() {
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //onTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val boolean = super.onTouchEvent(ev)

        return boolean
    }

    private fun getMotionLayout(): NestedScrollingParent2 {
        return parent as NestedScrollingParent2
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return getMotionLayout().onStartNestedScroll(child, target, axes, type)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        getMotionLayout().onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        getMotionLayout().onStopNestedScroll(target, type)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        getMotionLayout().onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        getMotionLayout().onNestedPreScroll(target, dx, dy, consumed, type)
    }

}