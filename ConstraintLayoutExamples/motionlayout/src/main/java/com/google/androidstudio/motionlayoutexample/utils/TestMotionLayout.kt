package com.google.androidstudio.motionlayoutexample.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

class TestMotionLayout : MotionLayout {

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
        setTransitionListener(object: TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                TODO("Not yet implemented")
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                TODO("Not yet implemented")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                TODO("Not yet implemented")
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                TODO("Not yet implemented")
            }

        })
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
}