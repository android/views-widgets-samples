/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.google.androidstudio.motionlayoutexample.fragmentsdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.fragment.app.Fragment
import com.google.androidstudio.motionlayoutexample.R
import kotlinx.android.synthetic.main.main_activity.*

class FragmentExample2Activity: AppCompatActivity(), View.OnClickListener, MotionLayout.TransitionListener {

    // TODO: Extract the common code with FragmentExampleActivity
    private var lastProgress = 0f
    private var fragment: Fragment? = null
    private var last: Float = 0f
    private var mLastSwipe: Long = 0


    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        if (p3 - lastProgress > 0) {
            // from start to end
            val atEnd = Math.abs(p3 - 1f) < 0.1f
            if (atEnd && fragment is MainFragment) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction
                        .setCustomAnimations(R.animator.show, 0)
                fragment = ListFragment.newInstance().also {
                    transaction
                            .replace(R.id.container, it)
                            .commitNow()
                }
            }
        } else {
            // from end to start
            val atStart = p3 < 0.9f
            if (atStart && fragment is ListFragment) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction
                        .setCustomAnimations(0, R.animator.hide)
                fragment = MainFragment.newInstance().also {
                    transaction
                            .replace(R.id.container, it)
                            .commitNow()
                }
            }
        }
        lastProgress = p3
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        //Using this way this listener will not let the execute before 250 milli seconds and the problem of multi-triggering will be solved
        if (SystemClock.elapsedRealtime() - mLastSwipe < 250) {
            return
        }
        mLastSwipe = SystemClock.elapsedRealtime()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {

            fragment = MainFragment.newInstance().also {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, it)
                        .commitNow()
            }
        }
        //toggle.setOnClickListener(this)
        //toggleAnimation.setOnClickListener(this)
        motionLayout.setTransitionListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.toggle) {
            val transaction = supportFragmentManager.beginTransaction()
            fragment = if (fragment == null || fragment is MainFragment) {
                last = 1f
                transaction
                        .setCustomAnimations(R.animator.show, 0)
                SecondFragment.newInstance()
            } else {
                transaction
                        .setCustomAnimations(0, R.animator.hide)
                MainFragment.newInstance()
            }.also {
                transaction
                        .replace(R.id.container, it)
                        .commitNow()
            }
        }
    }

}
