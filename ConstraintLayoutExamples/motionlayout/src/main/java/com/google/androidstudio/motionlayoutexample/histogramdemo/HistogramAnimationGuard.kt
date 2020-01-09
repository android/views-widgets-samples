/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.google.androidstudio.motionlayoutexample.histogramdemo

import androidx.constraintlayout.motion.widget.MotionLayout
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Animation guard helper.
 */
class HistogramAnimationGuard {

    /**
     * @return true if animation should wait. False otherwise.
     */
    val wait: Boolean
    get() {
        if (interruptible) {
            return false
        }
        return animating.get()
    }

    private var interruptible: Boolean = false
    private val animating = AtomicBoolean(false)

    val animationListener: MotionLayout.TransitionListener = object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
            animating.set(true)
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
            animating.set(false)
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }
        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }
    }

    /**
     * @param interruptible true if animation is interruptible. I.e. Animation does not need to be
     * finished before the new one starts.
     * False if animation must complete before new one can start.
     */
    fun setInterruptible(interruptible: Boolean) {
        this.interruptible = interruptible
    }
}