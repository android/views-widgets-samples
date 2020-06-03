package com.google.androidstudio.motionlayoutexample.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingParent2
import androidx.viewpager.widget.ViewPager

class TouchViewPager : ViewPager, NestedScrollingChild2 {

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

        return true
    }

    private fun getMotionLayout(): NestedScrollingParent2 {
        return parent as NestedScrollingParent2
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