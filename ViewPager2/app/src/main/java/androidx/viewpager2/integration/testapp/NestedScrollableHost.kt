/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.viewpager2.integration.testapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * Class to scroll a scrollable component inside a ViewPager2. Provided as a solution to the problem
 * where pages of ViewPager2 have nested scrollable elements that scroll in the same direction as
 * ViewPager2.
 *
 * This solution has limitations when using multiple levels of nested scrollable elements
 * (e.g. a horizontal RecyclerView in a vertical RecyclerView in a horizontal ViewPager2).
 */
private class NestedScrollableHost(context: Context) : View.OnTouchListener {

    private var touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var initialX = 0f
    private var initialY = 0f
    private val View.parentViewPager: ViewPager2?
        get() = generateSequence(this as? ViewParent, ViewParent::getParent)
            .filterIsInstance<ViewPager2>()
            .firstOrNull()

    private fun View.canScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            ViewPager2.ORIENTATION_HORIZONTAL -> canScrollHorizontally(direction)
            ViewPager2.ORIENTATION_VERTICAL -> canScrollVertically(direction)
            else -> throw IllegalArgumentException()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, e: MotionEvent): Boolean {
        handleInterceptTouchEvent(view, e)
        return false
    }

    private fun handleInterceptTouchEvent(view: View, e: MotionEvent) {
        val orientation = view.parentViewPager?.orientation ?: return

        // Early return if child can't scroll in same direction as parent
        if (!view.canScroll(orientation, -1f) && !view.canScroll(orientation, 1f)) {
            return
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            view.parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY
            val isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL

            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // Gesture is perpendicular, allow all parents to intercept
                    view.parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    // Gesture is parallel, query child if movement in that direction is possible
                    if (view.canScroll(orientation, if (isVpHorizontal) dx else dy)) {
                        // Child can scroll, disallow all parents to intercept
                        view.parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        // Child cannot scroll, allow all parents to intercept
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
    }
}

fun ViewGroup.allowSameDirectionScrollingInViewPager2() =
    setOnTouchListener(NestedScrollableHost(context))
